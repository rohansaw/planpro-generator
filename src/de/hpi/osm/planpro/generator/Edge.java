package de.hpi.osm.planpro.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Edge {
	private final Node nodeA;
	private final Node nodeB;
	private final List<GeoEdge> geoEdges;
	private final String topKanteUUID;

	public Edge(final Node nodeA, final Node nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.geoEdges = new ArrayList<>();
		this.geoEdges.add(new GeoEdge(this.nodeA.getX(), this.nodeA.getY(), this.nodeB.getX(), this.nodeB.getY()));
		this.topKanteUUID = Generator.getRandomUUID();
	}

	public Node getNodeA() {
		return this.nodeA;
	}

	public Node getNodeB() {
		return this.nodeB;
	}

	public boolean isNodeConnected(final Node node) {
		return this.getNodeA().equals(node) || this.getNodeB().equals(node);
	}

	public Node getOtherNode(final Node node) {
		if (this.getNodeA().equals(node)) {
			return this.getNodeB();
		}
		return this.getNodeA();
	}

	public List<String> getGeoKanteUUIDs() {
		return this.geoEdges.stream().map(geoEdge -> geoEdge.getGeoKanteUUID()).collect(Collectors.toList());
	}

	public String getTopKanteUUID() {
		return this.topKanteUUID;
	}

	public List<String> getUUIDs() {
		final List<String> uuids = this.getGeoKanteUUIDs();
		uuids.add(this.getTopKanteUUID());
		return uuids;
	}

	public void setCoordinatesString(final String coordinatesString) {
		this.geoEdges.clear();

		final List<String> coordinates = new ArrayList<>();
		coordinates.add(this.nodeA.getX() + "," + this.nodeA.getY());
		final String[] splits = coordinatesString.split(";");
		for (final String split : splits) {
			coordinates.add(split);
		}
		coordinates.add(this.nodeB.getX() + "," + this.nodeB.getY());

		for (int i = 0; i < coordinates.size() - 1; i++) {
			final String curCoords = coordinates.get(i);
			final double curX = Double.parseDouble(curCoords.split(",")[0]);
			final double curY = Double.parseDouble(curCoords.split(",")[1]);

			final String nextCoords = coordinates.get(i + 1);
			final double nextX = Double.parseDouble(nextCoords.split(",")[0]);
			final double nextY = Double.parseDouble(nextCoords.split(",")[1]);

			final GeoEdge geoEdge = new GeoEdge(curX, curY, nextX, nextY);
			this.geoEdges.add(geoEdge);
		}
	}

	public double getLength() {
		return this.geoEdges.stream().mapToDouble(geoEdge -> geoEdge.getLength()).sum();
	}
}
