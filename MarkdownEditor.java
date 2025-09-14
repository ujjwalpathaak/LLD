import java.util.ArrayList;
import java.util.List;

abstract class Element{
    public abstract String render();
}

abstract class Database{
    public abstract void save(String content);
}

class Editor {
    private MarkdownFile file;
    private Persistence storage;

    public Editor(MarkdownFile file, Persistence storage){
        this.file = file;
        this.storage = storage;
    }

    public void addText(String text){
        this.file.addElement(new Text(text));
    }

    public void addImage(String path, String imageName){
        imageName = (imageName != null) ? imageName : "unnamed";
        this.file.addElement(new Image(path, imageName));
    }

    public void preview(){
        String content = this.file.render();
        System.out.println("---- preview ----");
        System.out.println(content);
        System.out.println("---- preview ----");
    }

    public void save(Database connector) {
        this.storage.save(connector, this.file.render());
    }
}

class MarkdownFile {
    private List<Element> content = new ArrayList<>();

    public void addElement(Element element){
        content.add(element);
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (Element element : content) {
            sb.append(element.render()).append("\n");
        }
        return sb.toString();
    }
}


class Persistence{
    public void save(Database connector, String content){
        connector.save(content);
    }
}

class Renderer{
}

class SQLConnector extends Database {
    @Override
    public void save(String content) {
        System.out.println();
        System.out.println("Saving content to SQL Database...");
        System.out.println("Saved in SQL Database!");
    }
}

class MongoDBConnector extends Database {
    @Override
    public void save(String content) {
        System.out.println();
        System.out.println("Saving content to MongoDB Database...");
        System.out.println("Saved in MongoDB Database!");
    }
}

class Text extends Element{
    private String value = "";

    public Text(String text){
        this.value = text;
    }

    @Override
    public String render(){
        return this.value;
    }
}

class Image extends Element{
    private String path = "";
    private String imageName = "";

    public Image(String path, String imageName){
        this.path = path;
        this.imageName = imageName;
    }

    @Override
    public String render(){
        return "(" + this.imageName + ")" + "[" + this.path + "]";
    }
}

class UnorderedList extends Element{
    private String type = "-";
    private List<String> items = new ArrayList<>();

    public UnorderedList(List<String> items){
        this.items = items;
    }

    @Override
    public String render(){
        String renderedList = "";
        for (String item : this.items) {
            renderedList += type + " " + item + '\n';
        }

        return renderedList;
    }
}

class OrderedList extends Element{
    private String type = "numeric";
    private List<String> items = new ArrayList<>();

    public OrderedList(List<String> items){
        this.items = items;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        if ("numeric".equals(this.type)) {
            for (int i = 0; i < items.size(); i++) {
                sb.append(i + 1).append(". ").append(items.get(i)).append("\n");
            }
        }
        return sb.toString();
    }
}

public class MarkdownEditor {
    public static void main(String[] args) {
        MarkdownFile file = new MarkdownFile();
        Persistence storage = new Persistence();

        Editor editor = new Editor(file, storage);
        editor.addText("Heading");
        editor.addText("Title 1");
        editor.addImage("path/to/image", "image1");
        editor.addText("Title 2");
        editor.addText("text");
        editor.addText("Title 3");
        List<String> unorderedItems = List.of("Item 1", "Item 2", "Item 3");
        file.addElement(new UnorderedList(unorderedItems));
        editor.addText("Title 4");
        List<String> orderedItems = List.of("Step 1", "Step 2");
        file.addElement(new OrderedList(orderedItems));
        
        editor.preview();

        SQLConnector sqlDB = new SQLConnector();
        editor.save(sqlDB);
        MongoDBConnector mongoDB = new MongoDBConnector();
        editor.save(mongoDB);
    }
}