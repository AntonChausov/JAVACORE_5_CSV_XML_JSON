import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class MyStringMatcher extends TypeSafeDiagnosingMatcher<String> {
    public void describeTo(Description description) {
        description.appendText("Contains \"John\" and \"Ivan\"");
    }

    public static MyStringMatcher containJohnAndIvan() {
        return new MyStringMatcher();
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
        boolean resultJhon;
        boolean resultIvan;
        resultJhon = item.contains("John");
        resultIvan = item.contains("Ivan");
        if(!resultJhon){
            mismatchDescription.appendText("can't find \"Jhon\"");
        }
        if(!resultIvan){
            mismatchDescription.appendText((!resultJhon ? ", and " : "") + "can't find \"Ivan\"");
        }
        return resultJhon && resultIvan;
    }
}
