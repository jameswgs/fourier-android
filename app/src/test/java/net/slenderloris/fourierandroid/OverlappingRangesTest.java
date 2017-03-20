package net.slenderloris.fourierandroid;

import net.slenderloris.fourierandroid.OverlappingRanges.Window;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OverlappingRangesTest {

    // "1,2,3"

    @Test
    public void findIteratorHasNext() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        Iterator<Window> iterator = range.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void findIteratorFirstStartIndexIsZero() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        Iterator<Window> iterator = range.iterator();
        assertEquals(0, iterator.next().startIndex);
    }

    @Test
    public void findIteratorFirstEndIndexIs2() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        Iterator<Window> iterator = range.iterator();
        assertEquals(2, iterator.next().endIndex);
    }

    @Test
    public void findIteratorSecondStartIndexIs1() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        Iterator<Window> iterator = range.iterator();
        iterator.next();
        assertEquals(1, iterator.next().startIndex);
    }

    @Test
    public void findIteratorSecondEndIndexIs3() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        Iterator<Window> iterator = range.iterator();
        iterator.next();
        assertEquals(3, iterator.next().endIndex);
    }

    @Test
    public void findIteratorSecondHasNoNext() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        Iterator<Window> iterator = range.iterator();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void allWindowsWith3and2() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(3, 2);
        List<Window> list = toList(range);
        assertArrayEquals(new Window[] {
                new Window(0,2),
                new Window(1,3)
        }, list.toArray());
    }

    @Test
    public void allWindowsWith12and3() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(12, 3);
        List<Window> list = toList(range);
        assertArrayEquals(new Window[] {
                new Window(0,6),
                new Window(3,9),
                new Window(6,12)
        }, list.toArray());
    }

    @Test
    public void allWindowsWith20and4() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(20, 4);
        List<Window> list = toList(range);
        assertArrayEquals(new Window[] {
                new Window(0,8),
                new Window(4,12),
                new Window(8,16),
                new Window(12,20)
        }, list.toArray());
    }

    @Test
    public void allWindowsWith31and7() throws Exception {
        OverlappingRanges.Windows range = OverlappingRanges.find(31, 7);
        List<Window> list = toList(range);
        assertEquals(7,list.size());
        assertEquals(31,list.get(list.size()-1).endIndex);
    }

    private List<Window> toList(OverlappingRanges.Windows range) {
        List<Window> arrayList = new ArrayList<>();
        for (Window window : range) {
            arrayList.add(window);
        }
        return arrayList;
    }


}