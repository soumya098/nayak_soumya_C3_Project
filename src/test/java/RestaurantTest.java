import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    //REFACTOR ALL THE REPEATED LINES OF CODE
    @BeforeEach
    public void setUp(){
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        LocalTime currentTime = LocalTime.parse("11:30:00");
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        Mockito.when(spyRestaurant.getCurrentTime()).thenReturn(currentTime);
        assertTrue(spyRestaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        LocalTime currentTime = LocalTime.parse("09:30:00");
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        Mockito.when(spyRestaurant.getCurrentTime()).thenReturn(currentTime);
        assertFalse(spyRestaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Test
    public void show_total_order_value_should_be_388() throws itemNotFoundException{
        restaurant.addToMenu("Sizzling brownei", 319);
        List<String> items = List.of("Sweet corn soup", "Vegetable lasagne");

        assertEquals(restaurant.totalOrderValue(items), 388);
    }

    @Test
    public void total_order_value_should_be_0_when_no_items_are_selected() throws itemNotFoundException {
        List<String> items = List.of();
        assertEquals(restaurant.totalOrderValue(items), 0);
    }

    @Test
    public void total_order_value_should_throw_exception_when_item_is_not_in_menu() {
        List<String> items = List.of("Sweet corn soup", "Vegetable lasagne", "French fries");
        assertThrows(itemNotFoundException.class, () -> restaurant.totalOrderValue(items));
    }

    @Test
    public void display_details_should_return_correct_string() {
        String expectedOutput = "Restaurant:Amelie's cafe\nLocation:Chennai\nOpening time:10:30\nClosing time:22:00\nMenu:\n" + restaurant.getMenu();
        restaurant.displayDetails();
        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
    }

}
