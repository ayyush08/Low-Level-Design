# Bridge Pattern

Let us consider the example of a Music Playing scenario across different platforms or devices in certain PLayQuality.

```java
interface PlayQuality{
    void play(String title);
}

class WebHDPlayer implements PlayQuality{
    public void play(String title){
        System.out.println("Playing " + title + " in HD on Web");
    }
}
class MobileHDPlayer implements PlayQuality{
    public void play(String title){
        System.out.println("Playing " + title + " in HD on Mobile");
    }
}

class SmartTVUltraHDPlayer implements PlayQuality{
    public void play(String title){
        System.out.println("Playing " + title + " in Ultra HD on Smart TV");
    }
}
```

Now if tomorrow we want to add a new platform like SmartWatch, we will have to create a new class for each PlayQuality like SmartWatchHDPlayer, SmartWatchUltraHDPlayer, etc. If we have new play qualities like 4K, 8K, etc. we will have to create new classes for each platform and play quality combination. This will lead to a combinatorial explosion of classes.

To avoid this, we can use the Bridge pattern.

## Definition

It is a structural design pattern that decouples an abstraction from its implementation so that the two can vary independently.

## Real Life Analogy

Appliances & Remote Controls: We always have dedicated remote controls for each appliance like TV, AC, etc. 

## Product Example - Streaming Platform

```java
interface VideoQuality{
    void load(String title);
}

class SDQuality implements VideoQuality{
    public void load(String title){
        System.out.println("Streaming " + title + " in SD Quality");
    }
}

class HDQuality implements VideoQuality{
    public void load(String title){
        System.out.println("Streaming " + title + " in HD Quality");
    }
}

class UltraHDQuality implements VideoQuality{
    public void load(String title){
        System.out.println("Streaming " + title + " in Ultra HD Quality");
    }
}

abstract class VideoPlayer{
    protected VideoQuality videoQuality;

    public VideoPlayer(VideoQuality videoQuality){
        this.videoQuality = videoQuality;
    }

    abstract void play(String title);
}

class WebPlayer extends VideoPlayer{
    public WebPlayer(VideoQuality videoQuality){
        super(videoQuality);
    }

    public void play(String title){
        System.out.println("Playing on Web");
        videoQuality.load(title);
    }
}
class MobilePlayer extends VideoPlayer{
    public MobilePlayer(VideoQuality videoQuality){
        super(videoQuality);
    }

    public void play(String title){
        System.out.println("Playing on Mobile");
        videoQuality.load(title);
    }
}

public class Main{
    public static void main(String[] args) {
        VideoPlayer videoPlayer1 = new WebPlayer(new HDQuality());
        videoPlayer1.play("Inception");

        VideoPlayer videoPlayer2 = new MobilePlayer(new UltraHDQuality());
        videoPlayer2.play("Interstellar");
    }
}
```

Now, if we want to add a new platform like SmartWatch, we can simply create a new class SmartWatchPlayer that extends VideoPlayer and pass the desired VideoQuality implementation. 
Similarly, if we want to add a new VideoQuality like 4K, we can create a new class 4KQuality that implements VideoQuality and use it with any existing platform without creating a new class for each combination. 

This way, we can easily extend the functionality without modifying existing code, adhering to the Open/Closed Principle as well as the Single Responsibility Principle. 


## When to use Bridge Pattern

- You have 2 dimensions and they are tightly coupled. You want to decouple them so that they can vary independently.

- You want to evolve the abstraction and implementation independently.

- You want to avoid class explosion due to multiple combinations of abstraction and implementation.


## Pros

- **Decoupling**: The Bridge pattern decouples the abstraction from its implementation, so any change in either side does not affect the other side.

- **Avoids Class Explosion**: The Bridge pattern avoids class explosion by allowing you to create new abstractions and implementations independently.

- **Supports Open/Closed Principle**: The Bridge pattern supports the Open/Closed Principle by allowing you to extend the functionality without modifying existing code.

- **Cross-Platform Development**: The Bridge pattern allows you to create cross-platform applications by decoupling the platform-specific implementation from the abstraction.

= **Maintainability & Testability**: The Bridge pattern improves maintainability and testability by allowing you to test the abstraction and implementation independently.

## Cons

= **Increased Complexity**: The Bridge pattern can increase the complexity of the codebase by introducing additional layers of abstraction.

- **Confusion with other patterns**: The Bridge pattern can be confused with other patterns like Adapter, Decorator, and Strategy, which can lead to confusion and misuse.

- **Needs coordination between teams**: The Bridge pattern requires coordination between teams if the abstraction and implementation are developed by different teams, which can lead to communication overhead and delays.


## Class Diagram

```mermaid
classDiagram
    
    class VideoQuality{
        <<interface>>
        +load(title)
    }
    class SDQuality{
        +load(title)
    }
    class HDQuality{
        +load(title)
    }
    class UltraHDQuality{
        +load(title)
    }
    class VideoPlayer{
        <<abstract>>
        -videoQuality: VideoQuality
        +play(title)
    }
    class WebPlayer{
        +play(title)
    }
    class MobilePlayer{
        +play(title)
    }
    class StreamingPlatform{
        +main(args)
    }

    StreamingPlatform ..> WebPlayer : dependency
    StreamingPlatform ..> MobilePlayer : dependency
    StreamingPlatform ..> SDQuality : dependency
    StreamingPlatform ..> HDQuality : dependency
    StreamingPlatform ..> UltraHDQuality : dependency

    WebPlayer --|> VideoPlayer : Inheritance
    MobilePlayer --|> VideoPlayer : Inheritance

    SDQuality  ..|> VideoQuality : implements
    HDQuality  ..|> VideoQuality : implements
    UltraHDQuality  ..|> VideoQuality : implements

    VideoPlayer *-- VideoQuality : Composition (HAS-A)