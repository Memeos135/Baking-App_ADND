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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsTest {
    @Rule
    public final IntentsTestRule<RecipeDetails> rule =
            new IntentsTestRule<RecipeDetails>(RecipeDetails.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, RecipeDetails.class);
                    result.putExtra("test", "Earth");
                    return result;
                }
            };

    @Test
    public void test() throws Exception{
        onView(withId(R.id.fragment_text)).perform(click());
        intended(hasComponent(StepDetails.class.getName()));
    }
}
