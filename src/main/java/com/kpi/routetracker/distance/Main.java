package com.kpi.routetracker.distance;

public class Main {
    public static void main(String[] args) {
//        String startPoint = "Vinnytsia";
//
//        List<Parcel> parcels = new ArrayList<>();
//        parcels.add(new Parcel(1L, null, "Krefeld", null, 2, null, null, ParcelStatus.CREATED));
//        parcels.add(new Parcel(2L, null, "Wroclaw", null, 1, null, null, ParcelStatus.CREATED));
//        parcels.add(new Parcel(3L, null, "Cologne", null, 3, null, null, ParcelStatus.CREATED));
//
//        TravelingSalesman tsp = new TravelingSalesman();
//
//        Vertex vinnytsia = new Vertex("Vinnytsia");
//        Vertex krefeld = new Vertex("Krefeld");
//        Vertex wroclaw = new Vertex("Wroclaw");
//        Vertex cologne = new Vertex("Cologne");
//
//        vinnytsia.addEdge(new Edge(krefeld, 1835 ));
//        vinnytsia.addEdge(new Edge(wroclaw, 968));
//        vinnytsia.addEdge(new Edge(cologne, 1779));
//
//        krefeld.addEdge(new Edge(vinnytsia, 1835));
//        krefeld.addEdge(new Edge(wroclaw, 847 ));
//        krefeld.addEdge(new Edge(cologne, 57));
//
//        wroclaw.addEdge(new Edge(vinnytsia, 968));
//        wroclaw.addEdge(new Edge(krefeld, 847));
//        wroclaw.addEdge(new Edge(cologne, 829));
//
//        cologne.addEdge(new Edge(vinnytsia, 1779));
//        cologne.addEdge(new Edge(krefeld, 57));
//        cologne.addEdge(new Edge(wroclaw, 829));
//
//        tsp.addVertex(vinnytsia);
//        tsp.addVertex(krefeld);
//        tsp.addVertex(wroclaw);
//        tsp.addVertex(cologne);
//
//        List<Vertex> path = tsp.nearestNeighbor(startPoint);
//
//        System.out.println("Optimal path:");
//        for (Vertex vertex : path) {
//            System.out.println(vertex.getName());
//        }

//        String startPoint = "Vinnytsia";
//
//        List<Parcel> parcels = new ArrayList<>();
//        parcels.add(new Parcel(1L, null, "Krefeld", null, 2, null, null, ParcelStatus.CREATED));
//        parcels.add(new Parcel(2L, null, "Wroclaw", null, 1, null, null, ParcelStatus.CREATED));
//        parcels.add(new Parcel(3L, null, "Cologne", null, 3, null, null, ParcelStatus.CREATED));
//
//        TravelingSalesman tsp = new TravelingSalesman();
//
//        List<Vertex> vertexList = new ArrayList<>();
//        vertexList.add(new Vertex(startPoint));
//        for (Parcel parcel : parcels) {
//            vertexList.add(new Vertex(parcel.getDistinctionCity()));
//        }
//
//        for (int i = 0; i < vertexList.size(); i++) {
//            for (int k = 0; k < vertexList.size(); k++) {
//                if (k != i) {
//                    vertexList.get(i).addEdge(new Edge(vertexList.get(k), 1));
//                }
//
//            }
//
//        }
//
//        for (Vertex vertex : vertexList) {
//            tsp.addVertex(vertex);
//        }
    }
}
