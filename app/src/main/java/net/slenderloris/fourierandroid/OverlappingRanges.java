package net.slenderloris.fourierandroid;

import java.util.Iterator;

class OverlappingRanges {
    public static Windows find(int elementCount, int numWindows) {
        float numNonOverlappingWindows = (int) ((numWindows+1)/2.0f);
        if(numWindows%2==0) {
            numNonOverlappingWindows+=0.5f;
        }
        float windowSize = elementCount / numNonOverlappingWindows;
        return new Windows(windowSize, elementCount);
    }

    public static class Windows implements Iterable<Window> {

        private final float windowSize;
        private final int elementCount;

        public Windows(float windowSize, int elementCount) {
            this.windowSize = windowSize;
            this.elementCount = elementCount;
        }

        @Override
        public Iterator<Window> iterator() {
            return new WindowIterator(windowSize, elementCount);
        }

        private static class WindowIterator implements Iterator<Window> {

            private final float step;
            private final float windowSize;
            private final int numSamples;

            private float start = 0;

            public WindowIterator(float windowSize, int numSamples) {
                this.windowSize = windowSize;
                this.step = windowSize*0.5f;
                this.numSamples = numSamples;
            }

            @Override
            public boolean hasNext() {
                return (start+windowSize)<=numSamples;
            }

            @Override
            public Window next() {
                Window window = new Window((int) start, (int) (start + windowSize));
                start+=step;
                return window;
            }
        }
    }

    public static class Window {
        final int startIndex;
        final int endIndex;

        public Window(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Window window = (Window) o;
            if (startIndex != window.startIndex) return false;
            return endIndex == window.endIndex;
        }

        @Override
        public int hashCode() {
            int result = startIndex;
            result = 31 * result + endIndex;
            return result;
        }
    }
}
