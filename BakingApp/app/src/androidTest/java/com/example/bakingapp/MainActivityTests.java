package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTests {
    // THIS PORTION (TESTING) HAS BEEN TAKEN FROM AN ONLINE SOURCE

    @Rule
    public final IntentsTestRule<MainActivity> rule =
            new IntentsTestRule<MainActivity>(MainActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra("test", "Earth");
                    return result;
                }
            };

    @Test
    public void test() throws Exception {
        onView(withId(R.id.textView)).check(matches(withText("Earth")));
    }

    @Test
    public void testTwo() throws Exception {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void testThree() throws Exception {
        onView((withId(R.id.recyclerView))).perform(click());

        intended(hasComponent(RecipeDetails.class.getName()));
    }

    @Test
    public void testFour() throws Exception{
        onView(withId(R.id.textView)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText("Espresso")));
    }
}
