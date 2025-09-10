import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class ProjectTest {

    @Test
    public void checkCreateProject(){
       open("/login");

    }
}
