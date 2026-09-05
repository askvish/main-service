import module java.logging;

class DBConfig {
//    private final StableValue<String> dbUrl = StableValue.of();
//
//    public String getDbUrl() {
//        return dbUrl.orElseSet(
//                () -> loadDBUrl();
//        );
//    }
}

void main() {
    Logger log = Logger.getGlobal();
    IO.println("Hello from java 25");

    log.info("Hello from logger");
    log.log(Level.ALL, "Hello from logger");
    log.severe("Hello from logger");
    log.severe("Hello from logger");

//    newPrint();

    Employee ashok = new Employee("Ashok");

    IO.println(ashok);

    List<Employee> employees = new ArrayList<>(List.of(
            new Employee("Ashok"),
            new Employee("Aditi"),
            new Employee("Ram"),
            new Employee("Sita")
    ));

    List<String> names = new ArrayList<>();
    names.add("Earth");
    names.add("Mercury");
    names.add("Venus");
    names.add("Jupiter");

    names
            .forEach(
                    name -> employees.add(new Employee(name))
            );

    IO.println(employees);
}

void newPrint() {
    IO.println("New way of printing: IO.println(\"Hello\")");
    String number = IO.readln("Enter a number: ");

    IO.print(number);
    IO.println();
}

class Person {
    public Person(String name) {
        IO.println("Creating person " + name);
    }
}

class Employee extends Person {

    String name;

    Employee(String name) {

//        this.clone();
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        super(name);
        this.name = name;
    }

    // Before Java 25
    // ERROR!
    // Main.java:28: error: call to super must be first statement in constructor
    //        super(name);
    //             ^
    // 1 error

    @Override
    public String toString() {
        return this.name;
    }
}
