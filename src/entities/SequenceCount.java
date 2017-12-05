package entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequenceCount {
    private Map<List<Integer>, Integer> sequenceCount;

    public SequenceCount() {
        sequenceCount = new HashMap<>();
    }

    public void addSequence(List<Integer> sequence) {
        if (sequenceCount.containsKey(sequence)) {
            int previousValue = sequenceCount.get(sequence);
            sequenceCount.replace(sequence, previousValue + 1);
        } else {
            sequenceCount.put(sequence, 1);
        }
    }

    public long getTotalCount() {
        long sum = 0;
        for (int count : sequenceCount.values()) {
            sum += count;
        }
        return sum;
    }

    public Map<List<Integer>, Double> getSequenceProportion() {
        Map<List<Integer>, Double> sequenceProportion = new HashMap<>();
        long sum = getTotalCount();
        for (Map.Entry<List<Integer>, Integer> entry : sequenceCount.entrySet()) {
            sequenceProportion.put(entry.getKey(), entry.getValue() / (double)sum);
        }
        return sequenceProportion;
    }
}
