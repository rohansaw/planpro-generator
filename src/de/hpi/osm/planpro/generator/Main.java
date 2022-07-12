package de.hpi.osm.planpro.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

	private static ArrayList<Node> nodes = new ArrayList<>();

	private static ArrayList<Edge> edges = new ArrayList<>();

	private static ArrayList<Signal> signals = new ArrayList<>();

	public static void main(final String[] args) {
		System.out.println("Welcome to the PlanPro Generator");
		System.out.println("Usage:");
		System.out.println("Create a node (end or point): node <id> <x> <y> [--desc <description>]");
		System.out.println("Create an edge: edge <node id a> <node id b> [--coords <coordinates>]");
		System.out.println(
				"Create a signal: signal <start node id> <end node id> <distance from start node> [--func <function>] [--kind <kind>] [--sdist <side distance>]");
		System.out.println("Generate the plan pro file: generate");
		System.out.println("Exit without generate: exit");
		System.out.println("Open https://github.com/arneboockmeyer/planpro-generator for more details");
		System.out.println();
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.print("Please enter the file name (without suffix): ");
			final String filename = reader.readLine();

			try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".input"))) {
				String command = "";
				while (!command.equals("generate") && !command.equals("exit")) {
					System.out.print("#: ");
					command = reader.readLine();
					boolean isValid = false;
					if (command.isEmpty()) {
						continue;
					} else if (command.matches("node [a-zA-Z_0-9]+ -?\\d+(\\.\\d+)? -?\\d+(\\.\\d+)?.*")) {
						isValid = processNodeCommand(command);
					} else if (command.matches("edge [a-zA-Z_0-9]+ [a-zA-Z_0-9]+.*")) {
						isValid = processEdgeCommand(command);
					} else if (command.matches("signal [a-zA-Z_0-9]+ [a-zA-Z_0-9]+ -?\\d+(\\.\\d+)?.*")) {
						isValid = processSignalCommand(command);
					} else if (!command.equals("generate") && !command.equals("exit")) {
						System.out.println("Command does not exists.");
					}

					if (isValid) {
						writer.write(command + "\n");
					}
				}
				if (command.equals("generate")) {
					generate(filename);
				}
				System.out.println("Generator terminates.");
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	private static boolean processNodeCommand(final String command) {
		final String[] splits = command.split(" ");
		final String id = splits[1];

		if (nodes.stream().anyMatch(node -> node.getId().equals(id))) {
			System.out.println("Node with id " + id + " already exists. Please use a different id.");
			return false;
		}

		// Coordinates + shift in DB.Ref coordinate system
		final double x = Double.parseDouble(splits[2]) + 4533770.0;
		final double y = Double.parseDouble(splits[3]) + 5625780.0;

		final Node node = new Node(id, x, y);

		// Key Value Arguments
		final Map<String, String> arguments = getKeyValueArguments(command, 4);
		if (arguments.containsKey("desc")) {
			node.setDescription(arguments.get("desc"));
		}

		nodes.add(node);
		return true;
	}

	private static boolean processEdgeCommand(final String command) {
		final String[] splits = command.split(" ");
		final String nodeAID = splits[1];
		final String nodeBID = splits[2];

		final Node nodeA = nodes.stream().filter(node -> node.getId().equals(nodeAID)).findAny().orElse(null);
		final Node nodeB = nodes.stream().filter(node -> node.getId().equals(nodeBID)).findAny().orElse(null);
		if (nodeA == null) {
			System.out.println("Node with ID " + nodeAID + " does not exists. Please create it first.");
			return false;
		}
		if (nodeB == null) {
			System.out.println("Node with ID " + nodeBID + " does not exists. Please create it first.");
			return false;
		}
		// Edges are bidirectional
		final boolean alreadyConnected = nodeA.getConnectedNodes().stream()
				.anyMatch(connectedNode -> connectedNode.getId().equals(nodeB.getId()));
		if (alreadyConnected) {
			System.out.println("The nodes " + nodeAID + " and " + nodeBID + " are already connected.");
			return false;
		}

		final Edge edge = new Edge(nodeA, nodeB);

		// Key Value Arguments
		final Map<String, String> arguments = getKeyValueArguments(command, 3);
		if (arguments.containsKey("coords")) {
			edge.setCoordinatesString(arguments.get("coords"));
		}

		edges.add(edge);
		nodeA.getConnectedNodes().add(nodeB);
		nodeB.getConnectedNodes().add(nodeA);
		return true;
	}

	private static boolean processSignalCommand(final String command) {
		final String[] splits = command.split(" ");
		final String nodeAID = splits[1];
		final String nodeBID = splits[2];

		final Node nodeA = nodes.stream().filter(node -> node.getId().equals(nodeAID)).findAny().orElse(null);
		final Node nodeB = nodes.stream().filter(node -> node.getId().equals(nodeBID)).findAny().orElse(null);
		if (nodeA == null) {
			System.out.println("Node with ID " + nodeAID + " does not exists. Please create it first.");
			return false;
		}
		if (nodeB == null) {
			System.out.println("Node with ID " + nodeBID + " does not exists. Please create it first.");
			return false;
		}

		final Edge edge = edges.stream().filter(e -> e.getNodeA().equals(nodeA) && e.getNodeB().equals(nodeB)
				|| e.getNodeA().equals(nodeB) && e.getNodeB().equals(nodeA)).findAny().orElse(null);
		if (edge == null) {
			System.out.println("Node " + nodeAID + " and node " + nodeBID + " are not connected. Create edge first.");
			return false;
		}

		double distance = Double.parseDouble(splits[3]);
		if (distance > edge.getLength()) {
			System.out.println("Distance is greater than edge length. Choose a smaller distance.");
			return false;
		}

		String effectiveDirection = "in";
		if (edge.getNodeA().equals(nodeB) && edge.getNodeB().equals(nodeA)) {
			effectiveDirection = "gegen";
			distance = edge.getLength() - distance;
		}

		final Signal signal = new Signal(null, edge, distance, effectiveDirection);

		// Key Value Arguments
		final Map<String, String> arguments = getKeyValueArguments(command, 3);
		if (arguments.containsKey("func")) {
			final String function = arguments.get("func");
			if (Signal.supportedFunctions.contains(function)) {
				signal.setFunction(function);
			} else {
				System.out.println("Function \"" + function + "\" is not supported. Possible are: "
						+ String.join(",", Signal.supportedFunctions) + " Default value ("
						+ Signal.supportedFunctions.get(0) + ") will be used.");
			}
		}
		if (arguments.containsKey("kind")) {
			final String kind = arguments.get("kind");
			if (Signal.supportedKinds.contains(kind)) {
				signal.setKind(kind);
			} else {
				System.out.println("Kind \"" + kind + "\" is not supported. Possible are: "
						+ String.join(",", Signal.supportedKinds) + " Default value (" + Signal.supportedKinds.get(0)
						+ ") will be used.");
			}
		}
		if (arguments.containsKey("sdist")) {
			if (arguments.get("sdist").matches("\\d+(\\.\\d+)?")) {
				final double sideDistance = Double.parseDouble(arguments.get("sdist"));
				signal.setSideDistance(sideDistance);
			} else {
				System.out.println("Side distance \"" + arguments.get("sdist")
						+ "\" is not a valid value for the side distance. Default value ("
						+ Math.abs(signal.getSideDistance()) + ") will be used.");
			}
		}

		signals.add(signal);
		return true;

	}

	private static void generate(final String filename) {
		final ArrayList<Route> routes = new ArrayList<>();
		final Route route = new Route(edges);
		signals.forEach(signal -> signal.setRoute(route));
		routes.add(route);

		final ArrayList<RunningTrack> runningTracks = RunningTrack.generateRunningTracks(nodes, edges, signals);

		final Generator g = new Generator(nodes, edges, signals, routes, runningTracks);
		g.generate(filename);
		System.out.println("Generation completed");
	}

	private static Map<String, String> getKeyValueArguments(final String command, final int skipParts) {
		final Map<String, String> arguments = new HashMap<>();
		final String[] splits = command.split(" ");
		for (int i = skipParts; i < splits.length; i = i + 2) {
			if (i + 1 >= splits.length) {
				break;
			}
			final String key = splits[i];
			final String value = splits[i + 1];
			arguments.put(key.substring(2), value);
		}
		return arguments;
	}
}
