package NotificationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * Question:
 * You are designing a subsystem for a Recruitment Module. When a job
 * application’s status changes 
 * (e.g., Applied → Shortlisted → Interview Scheduled → Selected → Rejected),
 * multiple other services must react to this update.

Requirements:
	1.	When a job application’s status changes, the system must notifyServiceServices many independent components:
        •	EmailService → Sends an email to the candidate
        •	SMSService → Sends an SMS
        •	AnalyticsService → Logs events to analytics
        •	DashboardService → Updates recruiter dashboards
        •	AuditService → Writes entries to an audit log
        •	WebhookNotifier → Sends webhook notifications to client systems
	2.	These services must not be tightly coupled with the JobApplication class.
	3.	The system should allow adding new listeners (observers) without modifying existing application workflow.
	4.	Some observers may fail—your design should continue notifyServiceServicesing others.
	5.	Observers must be able to unsubscribe from updates dynamically.
	6.	Some observers want only certain events (e.g., AnalyticsService wants all events, but EmailService wants only final results).
 */

enum ApplicationState {
    APPLIED,
    SHORTLISTED,
    INTERVIEW_SCHEDULED,
    SELECTED,
    REJECTED;

    public String getState(){
        switch (this) {
            case APPLIED:
                return "Applied";
        
            case SHORTLISTED:
                return "Shortlisted";
        
            case INTERVIEW_SCHEDULED:
                return "Interview Scheduled";
        
            case SELECTED:
                return "Selected";

            case REJECTED:
                return "Rejected";
        
            default:
                throw new Error("Unknown State");
        }
    }
}

abstract class Service{
    abstract public void notifyService();
};

class RecruiterDashboard extends Service{
    public void notifyService(){
        System.out.println("Updated Recruiter Dashboard");
    }
};

class AnalyticsService extends Service{
    public void notifyService(){
        System.out.println("Updated Analytics");
    }
};

class AuditService extends Service{
    public void notifyService(){
        System.out.println("Added Audits");
    }
};

class EmailService extends Service{
    public void notifyService(){
        System.out.println("Email sent");
    }
};

class SMSService extends Service{
    public void notifyService(){
        System.out.println("SMS sent");
    }
};

class WebhookNotifier extends Service{
    public void notifyService(){
        System.out.println("Webhook sent");
    }
};

class EventBus {
    Map<ApplicationState, ArrayList<Service>> listeners = new HashMap<ApplicationState, ArrayList<Service>>();

    public void setListeners(ApplicationState state, ArrayList<Service> listeners){
        this.listeners.put(state, listeners);
    }

    public void removeListeners(ApplicationState state, ArrayList<Service> listeners){
        ArrayList<Service> existingListeners = this.listeners.get(state);
        for (Service service : listeners) {
            existingListeners.remove(service);
        }
        this.setListeners(state, existingListeners);
    }

    public void notifyService(ApplicationState state){
        ArrayList<Service> listeners = this.listeners.get(state);
        System.out.println("Application Moved to " + state.getState());
        for (Service listener : listeners) {
            listener.notifyService();
        }
        System.out.println();
    }
}

class JobApplication{
    ApplicationState currentState;
    EventBus eventBus;
    Map<ApplicationState, ApplicationState> transitions = new HashMap<>();

    public void setTransition(ApplicationState state1, ApplicationState state2){
        // validations on setting transactions eq (duplicate, circular)
        transitions.put(state1, state2);
    }

    public JobApplication(ApplicationState initialState, EventBus eventBus){
        this.currentState = initialState;
        this.eventBus = eventBus;
        this.eventBus.notifyService(currentState);
    }
    
    public void moveToNextStage(){
        this.currentState = transitions.get(this.currentState);
        this.eventBus.notifyService(currentState);
    }
};

public class Main {
    public static void main(String[] args){
        ApplicationState applied = ApplicationState.APPLIED;
        ApplicationState shortlisted = ApplicationState.SHORTLISTED;
        ApplicationState interview = ApplicationState.INTERVIEW_SCHEDULED;
        ApplicationState selected = ApplicationState.SELECTED;
        ApplicationState rejected = ApplicationState.REJECTED;

        EventBus eventBus = new EventBus();
        
        Service EmailService = new EmailService();
        Service SMSService = new SMSService();
        Service WebhookService = new WebhookNotifier();

        eventBus.setListeners(applied, new ArrayList<Service>(Arrays.asList(EmailService, SMSService)));
        eventBus.setListeners(shortlisted, new ArrayList<Service>(Arrays.asList(SMSService, WebhookService)));
        eventBus.setListeners(interview, new ArrayList<Service>(Arrays.asList(EmailService, SMSService, WebhookService)));
        eventBus.setListeners(selected, new ArrayList<Service>(Arrays.asList(EmailService, WebhookService)));
        eventBus.setListeners(rejected, new ArrayList<Service>(Arrays.asList(SMSService)));

        JobApplication application = new JobApplication(applied, eventBus);

        application.setTransition(applied, shortlisted);
        application.setTransition(shortlisted, interview);
        application.setTransition(interview, selected);
        application.setTransition(selected, rejected);

        application.moveToNextStage();
        
        eventBus.removeListeners(interview, new ArrayList<Service>(Arrays.asList(SMSService, WebhookService)));
        
        application.moveToNextStage();
    }
}

// ⚠️ 3. Problems Still Present (Interviewer will ask about these)

// These are the points that still need improvement.

// ❌ 3.1 setListeners() overwrites the entire list

// This means:

// eventBus.setListeners(state, listOf(A,B))


// If someone calls:

// eventBus.setListeners(state, listOf(C))


// Listeners A and B are lost.

// An interviewer will ask:

// “How do you add just one listener? Why replace the whole list?”

// Better:

// subscribe(state, listener)
// unsubscribe(state, listener)

// ❌ 3.2 removeListeners() mutates arrays dangerously

// You're modifying:

// ArrayList<Service> existingListeners = this.listeners.get(state);


// Then removing items inside a loop.

// Works, but brittle.

// ❌ 3.3 No exception handling

// Requirement says:

// “Some observers may fail — the system must continue notifying others.”

// Your code:

// listener.notifyService();


// No try/catch → one failure stops all notifications
// → This violates requirement #4.

// ❌ 3.4 No validation for transitions

// The comment says:

// // validations on setting transactions eq (duplicate, circular)


// But they are not implemented.

// Interviewer may ask:

// “What if someone sets APPLIED → APPLIED or creates a loop?”

// ❌ 3.5 EventBus should handle empty-listeners case

// If no listeners exist for a state → NullPointerException.

// Needs:

// ArrayList<Service> listeners = this.listeners.getOrDefault(state, new ArrayList<>());

// ❌ 3.6 Wrong naming: notifyService()

// Should be notify() or onStateChange().
// Not a big issue, but interviewers notice naming hygiene.