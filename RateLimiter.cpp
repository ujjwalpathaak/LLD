#include <iostream>
#include <string>
#include <unordered_map>
#include <ctime>
#include <thread>
#include <chrono>
using namespace std;

static int USER_ID = 0;

string nowStr() {
    time_t now = time(0);
    return to_string(now);
}

enum class UserTier {
    FREE,
    PREMIUM
};

enum class RateLimiterType {
    TOKEN_BASED,
    FIXED_WINDOW
};

class Config {
public:
    int maxRequests;
    int window;

    Config(int maxRequests = 0, int window = 0) {
        this->maxRequests = maxRequests;
        this->window = window;
    }
};

class User {
private:
    string userId;
    UserTier userTier;
    RateLimiterType rateLimiterType;

public:
    string id() const {
        return userId;
    }

    RateLimiterType rateLimiter() const {
        return rateLimiterType;
    }

    UserTier tier() const {
        return userTier;
    }

    User(UserTier tier, RateLimiterType rateLimiterType) {
        this->userId = to_string(USER_ID++);
        this->userTier = tier;
        this->rateLimiterType = rateLimiterType;
    }
};

class RateLimiter {
public:
    virtual bool allowRequest(string userId) = 0;
    virtual ~RateLimiter() {}
};

class ConfigService {
public:
    static Config getConfig(UserTier userTier, RateLimiterType rateLimiterType) {
        switch (userTier) {
            case UserTier::FREE:
                return Config(3, 5);   // small window for testing
            case UserTier::PREMIUM:
                return Config(5, 5);
        }
        return Config();
    }
};

class TokenBucketRateLimiter : public RateLimiter {
    Config config;
    int basket = 0;
    int lastRefillTime = 0;

public:
    TokenBucketRateLimiter(Config config) {
        this->config = config;
        basket = config.maxRequests;
        lastRefillTime = time(0);
    }

    bool allowRequest(string userId) override {
        int now = time(0);

        // Refill tokens
        int elapsed = now - lastRefillTime;
        if (elapsed > 0) { 
            int tokensToAdd = elapsed * config.maxRequests / config.window;
            basket = min(config.maxRequests, basket + tokensToAdd);
            lastRefillTime = now;
        }

        // Consume token
        if (basket > 0) {
            basket--;
            return true;
        }

        return false;
    }
};

class FixedWindowRateLimiter : public RateLimiter {
private:
    Config config;
    int currRequests = 0;
    int start = -1;
    int end = -1;

    void resetWindow(int now, string userId) {
        start = now;
        end = now + config.window;
        currRequests = 0;

        cout << "[" << nowStr() << "] "
             << "[User " << userId << "] Window Reset -> "
             << "Start=" << start << ", End=" << end << endl;
    }

public:
    FixedWindowRateLimiter(Config config) {
        this->config = config;
    }

    bool allowRequest(string userId) override {
        int now = time(0);

        if (start == -1 || now > end) {
            resetWindow(now, userId);
        }

        if (currRequests < config.maxRequests) {
            currRequests++;
            cout << "[" << nowStr() << "] "
                 << "[User " << userId << "] Allowed "
                 << "(" << currRequests << "/" << config.maxRequests << ")\n";
            return true;
        }

        cout << "[" << nowStr() << "] "
             << "[User " << userId << "] BLOCKED "
             << "(" << currRequests << "/" << config.maxRequests << ")\n";
        return false;
    }
};

class RateLimiterFactory {
public:
    static RateLimiter* getRateLimiter(RateLimiterType type, Config config) {
        switch (type) {
            case RateLimiterType::TOKEN_BASED:
                return new TokenBucketRateLimiter();
            case RateLimiterType::FIXED_WINDOW:
                return new FixedWindowRateLimiter(config);
        }
        return nullptr;
    }
};

class RateLimiterService {
private:
    unordered_map<string, RateLimiter*> userToRateLimiterMap;

public:
    bool allowRequest(User* user) {
        if (userToRateLimiterMap.find(user->id()) == userToRateLimiterMap.end()) {
            addUser(user);
        }

        return userToRateLimiterMap[user->id()]->allowRequest(user->id());
    }

    void addUser(User* user) {
        Config config = ConfigService::getConfig(user->tier(), user->rateLimiter());

        userToRateLimiterMap[user->id()] =
            RateLimiterFactory::getRateLimiter(user->rateLimiter(), config);

        cout << "[" << nowStr() << "] "
             << "Limiter added for user " << user->id()
             << " | max=" << config.maxRequests
             << " window=" << config.window << "s\n";
    }
};

int main() {
    RateLimiterService service;

    User* freeUser = new User(UserTier::FREE, RateLimiterType::FIXED_WINDOW);
    User* premiumUser = new User(UserTier::PREMIUM, RateLimiterType::FIXED_WINDOW);

    cout << "\n--- FREE USER TEST ---\n";
    for (int i = 0; i < 5; i++) {
        service.allowRequest(freeUser);
        this_thread::sleep_for(chrono::seconds(1));
    }

    cout << "\n--- WAIT FOR WINDOW RESET ---\n";
    this_thread::sleep_for(chrono::seconds(6));

    cout << "\n--- AFTER RESET ---\n";
    service.allowRequest(freeUser);

    cout << "\n--- PREMIUM USER TEST ---\n";
    for (int i = 0; i < 7; i++) {
        service.allowRequest(premiumUser);
        this_thread::sleep_for(chrono::milliseconds(500));
    }

    return 0;
}