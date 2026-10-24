//Technical way to implement Dependency Injection



//Dependency (what is needed) - always an interface
interface NotificationService {
    void sendNotification(String message);
}

//Concrete implementation
class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending email notification: " + message);
    }
}


//Client who needs the dependency

class UserService{
    private NotificationService notificationService;

    //Constructor Injection
    public UserService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void register(String user) {
        System.out.println("Registering user: " + user);
        notificationService.sendNotification("Welcome " + user + "!");
    }
}


class Main{
    public static void main(String[] args) {
        //Creating the dependency
        NotificationService notificationService = new EmailNotificationService();

        //Injecting the dependency into the client
        UserService userService = new UserService(notificationService);

        //Using the client
        userService.register("John Doe");
    }
}
