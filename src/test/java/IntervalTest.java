import org.junit.jupiter.api.Assertions;
import ru.bendricks.Interval;

public class IntervalTest {

    @org.junit.jupiter.api.Test
    public void testIntervalConstructor(){
        Assertions.assertEquals("Eb", Interval.intervalConstruction(new String[]{"P5", "Ab", Interval.ASCENDING}));
        Assertions.assertEquals("E#", Interval.intervalConstruction(new String[]{"P5", "A#", Interval.ASCENDING}));
        Assertions.assertEquals("Gbb", Interval.intervalConstruction(new String[]{"m2", "Fb"}));
        Assertions.assertEquals("E", Interval.intervalConstruction(new String[]{"P8", "E", Interval.ASCENDING}));
        Assertions.assertEquals("Eb", Interval.intervalConstruction(new String[]{"P8", "Eb", Interval.DESCENDING}));
        Assertions.assertEquals("Db", Interval.intervalConstruction(new String[]{"P5", "Ab", Interval.DESCENDING}));
        Assertions.assertEquals("D#", Interval.intervalConstruction(new String[]{"P5", "A#", Interval.DESCENDING}));
        Assertions.assertEquals("Eb", Interval.intervalConstruction(new String[]{"m2", "Fb", Interval.DESCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(null));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(new String[]{"P1", "Ab", Interval.ASCENDING, "hello"}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(new String[]{"P1"}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(new String[]{"P1", null, Interval.ASCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(new String[]{"P1", "Ab", Interval.ASCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(new String[]{"P5", "Ab#", Interval.ASCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalConstruction(new String[]{"P5", "S", Interval.ASCENDING}));
    }

    @org.junit.jupiter.api.Test
    public void testIntervalIdentifier(){
        Assertions.assertEquals("P5", Interval.intervalIdentification(new String[]{"Ab", "Eb", Interval.ASCENDING}));
        Assertions.assertEquals("P5", Interval.intervalIdentification(new String[]{"A#", "E#", Interval.ASCENDING}));
        Assertions.assertEquals("m2", Interval.intervalIdentification(new String[]{"Fb", "Gbb"}));
        Assertions.assertEquals("P8", Interval.intervalIdentification(new String[]{"E", "E", Interval.ASCENDING}));
        Assertions.assertEquals("P8", Interval.intervalIdentification(new String[]{"Eb", "Eb", Interval.DESCENDING}));
        Assertions.assertEquals("P5", Interval.intervalIdentification(new String[]{"Ab", "Db", Interval.DESCENDING}));
        Assertions.assertEquals("P5", Interval.intervalIdentification(new String[]{"A#", "D#", Interval.DESCENDING}));
        Assertions.assertEquals("m2", Interval.intervalIdentification(new String[]{"Fb", "Eb", Interval.DESCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(null));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(new String[]{"Eb", "S", Interval.ASCENDING, "hello"}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(new String[]{"Eb"}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(new String[]{"Eb", "Eb", null}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(new String[]{"Eb", "S", Interval.DESCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(new String[]{"E#b", "Eb", Interval.DESCENDING}));
        Assertions.assertThrows(IllegalArgumentException.class, ()->Interval.intervalIdentification(new String[]{"Eb", "Et", Interval.DESCENDING}));
    }

}
