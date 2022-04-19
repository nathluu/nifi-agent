package org.apache.nifi.agent.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the bounding box the Processing Group and some placement logic.
 */
public class ProcessGroupBox implements Comparable<ProcessGroupBox> {
    // values as specified in the nf-process-group.js file
    public static final int PG_SIZE_WIDTH = 380;
    public static final int PG_SIZE_HEIGHT = 172;
    // minimum whitespace between PG elements for auto-layout
    public static final int PG_SPACING = 50;

    private final int x;
    private final int y;

    public static final ProcessGroupBox CANVAS_CENTER = new ProcessGroupBox(0, 0);

    public ProcessGroupBox(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @return distance from a (0, 0) point.
     */
    public int distance() {
        // a simplified distance formula because the other coord is (0, 0)
        return (int) Math.hypot(x, y);
    }

    public boolean intersects(ProcessGroupBox other) {
        // this is completely left of other
        if (this.x + PG_SIZE_WIDTH < other.x) {
            return false;
        }

        // this is completely right of other
        if (this.x > other.x + PG_SIZE_WIDTH) {
            return false;
        }

        // this is completely above other
        if (this.y + PG_SIZE_HEIGHT < other.y) {
            return false;
        }

        // this is completely below other
        if (this.y > other.y + PG_SIZE_HEIGHT) {
            return false;
        }

        return true;
    }

    public ProcessGroupBox findFreeSpace(List<ProcessGroupBox> allCoords) {
        // sort by distance to (0.0)
        List<ProcessGroupBox> byClosest = allCoords.stream().sorted().collect(Collectors.toList());

        // search to the right
        List<ProcessGroupBox> freeSpots = byClosest.stream().filter(other ->
                byClosest.stream().noneMatch(other.right()::intersects)
        ).map(ProcessGroupBox::right).collect(Collectors.toList()); // save a 'transformed' spot 'to the right'

        // search down
        freeSpots.addAll(byClosest.stream().filter(other ->
                byClosest.stream().noneMatch(other.down()::intersects)
        ).map(ProcessGroupBox::down).collect(Collectors.toList()));

        // search left
        freeSpots.addAll(byClosest.stream().filter(other ->
                byClosest.stream().noneMatch(other.left()::intersects)
        ).map(ProcessGroupBox::left).collect(Collectors.toList()));

        // search above
        freeSpots.addAll(byClosest.stream().filter(other ->
                byClosest.stream().noneMatch(other.up()::intersects)
        ).map(ProcessGroupBox::up).collect(Collectors.toList()));

        // return a free spot closest to (0, 0)
        return freeSpots.stream().sorted().findFirst().orElse(CANVAS_CENTER);
    }

    public ProcessGroupBox right() {
        return new ProcessGroupBox(this.x + PG_SIZE_WIDTH + PG_SPACING, this.y);
    }

    public ProcessGroupBox down() {
        return new ProcessGroupBox(this.x, this.y + PG_SIZE_HEIGHT + PG_SPACING);
    }

    public ProcessGroupBox up() {
        return new ProcessGroupBox(this.x, this.y - PG_SPACING - PG_SIZE_HEIGHT);
    }

    public ProcessGroupBox left() {
        return new ProcessGroupBox(this.x - PG_SPACING - PG_SIZE_WIDTH, this.y);
    }

    @Override
    public int compareTo(ProcessGroupBox other) {
        return this.distance() - other.distance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessGroupBox pgBox = (ProcessGroupBox) o;
        return Double.compare(pgBox.x, x) == 0
                && Double.compare(pgBox.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
