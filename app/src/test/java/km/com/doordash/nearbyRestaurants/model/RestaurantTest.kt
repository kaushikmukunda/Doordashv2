package km.com.doordash.nearbyRestaurants.model

import org.junit.Assert.assertEquals
import org.junit.Test

class RestaurantTest {

    @Test
    fun `when description empty return empty string`() {
        val restaurant = Restaurant("", "", "", "", "")
        assertEquals("", restaurant.getDescription())
    }

    @Test
    fun `when description less than maxLen return description`() {
        val restaurant = Restaurant("", "", "small desc", "", "")
        assertEquals("small desc", restaurant.getDescription())
    }

    @Test
    fun `when description more than maxLen return description`() {
        val restaurant = Restaurant("", "", "very,long,description,longer than limit", "", "")
        assertEquals("very, long", restaurant.getDescription())
    }
}