package com.dsavisualizer.algorithms;

import com.dsavisualizer.structures.DataStructure;
import com.dsavisualizer.structures.LinkedListStructure;
import com.dsavisualizer.structures.QueueStructure;
import com.dsavisualizer.structures.StackStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * The ONE place that lists every algorithm and data structure of the project.
 *
 * The sidebar and the menu loop over these lists, so adding a new algorithm
 * = create its class + add ONE line here. Nothing else has to change.
 *
 * The methods are STATIC, so they are called on the class itself:
 * AlgorithmRegistry.getSortAlgorithms().
 */
public final class AlgorithmRegistry {

    private AlgorithmRegistry() {
    }

    public static List<SortAlgorithm> getSortAlgorithms() {

        List<SortAlgorithm> list = new ArrayList<>();

        list.add(new BubbleSort());
        list.add(new SelectionSort());
        list.add(new InsertionSort());
        list.add(new MergeSort());
        list.add(new QuickSort());
        list.add(new HeapSort());

        return list;
    }

    public static List<SearchAlgorithm> getSearchAlgorithms() {

        List<SearchAlgorithm> list = new ArrayList<>();

        list.add(new LinearSearch());
        list.add(new BinarySearch());

        return list;
    }

    public static List<DataStructure> getDataStructures() {

        List<DataStructure> list = new ArrayList<>();

        list.add(new StackStructure());
        list.add(new QueueStructure());
        list.add(new LinkedListStructure());

        return list;
    }

    public static List<GraphAlgorithm> getGraphAlgorithms() {

        List<GraphAlgorithm> list = new ArrayList<>();

        list.add(new BreadthFirstSearch());
        list.add(new DepthFirstSearch());
        list.add(new DijkstraAlgorithm());
        list.add(new KruskalAlgorithm());

        return list;
    }
}
