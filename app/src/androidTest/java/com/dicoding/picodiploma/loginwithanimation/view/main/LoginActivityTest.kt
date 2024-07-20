package com.dicoding.picodiploma.loginwithanimation.view.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.picodiploma.loginwithanimation.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign In"),
                childAtPosition(
                    allOf(
                        withId(R.id.background),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val emailEditText = onView(
            allOf(
                withId(R.id.ed_login_email),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailEditTextLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        emailEditText.perform(replaceText("narada2@mail.com"), closeSoftKeyboard())

        val passwordEditText = onView(
            allOf(
                withId(R.id.ed_login_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordEditTextLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        passwordEditText.perform(replaceText("123123123"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.loginButton), withText("Sign In"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        Thread.sleep(5000)

        val materialButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("Next"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        materialButton3.perform(scrollTo(), click())

        val recyclerView = onView(
            allOf(
                withId(R.id.rv_stories),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val imageButton = onView(
            allOf(
                withId(R.id.fab_add_story), withContentDescription("Create new post"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.ic_map), withContentDescription("Map"),
                withParent(withParent(withId(R.id.my_toolbar))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
