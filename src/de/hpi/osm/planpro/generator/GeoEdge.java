package de.hpi.osm.planpro.generator;

public class GeoEdge {

	private final double startX;
	private final double startY;
	private final double endX;
	private final double endY;
	private final String geoKanteUUID;

	public GeoEdge(final double startX, final double startY, final double endX, final double endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.geoKanteUUID = Generator.getRandomUUID();
	}

	public double getStartX() {
		return this.startX;
	}

	public double getStartY() {
		return this.startY;
	}

	public double getEndX() {
		return this.endX;
	}

	public double getEndY() {
		return this.endY;
	}

	public String getGeoKanteUUID() {
		return this.geoKanteUUID;
	}

	public double getLength() {
		final double minX = Math.min(this.startX, this.endX);
		final double maxX = Math.max(this.startX, this.endX);
		final double minY = Math.min(this.startY, this.endY);
		final double maxY = Math.max(this.startY, this.endY);
		return Math.sqrt(Math.pow(maxX - minX, 2) + Math.pow(maxY - minY, 2));
	}
}
