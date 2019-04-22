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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class SimpleTests {
    // THIS PORTION (TESTING) HAS BEEN TAKEN FROM AN ONLINE SOURCE

    @Rule
    public final IntentsTestRule<MainActivity> rule =
            new IntentsTestRule<MainActivity>(MainActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra("Name", "Earth");
                    return result;
                }
            };

    @Test
    public void test() throws Exception {
        onView(withId(R.id.textView)).check(matches(withText("Earth")));
    }
}
