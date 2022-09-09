from .utils import Config
from .planproxml import NodeXML, EdgeXML, SignalXML, RouteXML, RootXML, TripXML
from .model import Trip
from .routegenerator import RouteGenerator

import uuid


class Generator(object):

    def __init__(self):
        self.uuids = []
        self.geo_nodes = []
        self.geo_points = []
        self.top_nodes = []
        self.geo_edges = []
        self.top_edges = []
        self.signals = []
        self.control_elements = []
        self.trips = []
        self.routes = []
        self.config = None

    def generate(self, nodes, edges, signals, config: Config, filename=None):
        self.uuids = []
        self.geo_nodes = []
        self.geo_points = []
        self.top_nodes = []
        self.geo_edges = []
        self.top_edges = []
        self.signals = []
        self.control_elements = []
        self.trips = []
        self.routes = []
        self.config = config

        # Create Trip
        trip = Trip(edges)
        for signal in signals:
            signal.trip = trip

        # Create Routes
        route_generator = RouteGenerator(nodes, edges, signals)
        routes = route_generator.generate_routes()

        self.uuids = self.uuids + RootXML.get_root_uuids()

        self.generate_nodes(nodes)
        self.generate_edges(edges)
        self.generate_signals(signals)
        self.generate_trips([trip])
        self.generate_routes(routes)

        result_string = ""
        result_string = result_string + RootXML.get_prefix_xml()
        result_string = result_string + RootXML.get_external_element_control_xml()

        def add_list_to_result_string(_list):
            nonlocal result_string
            for entry in _list:
                result_string = result_string + entry

        add_list_to_result_string(self.routes)
        add_list_to_result_string(self.geo_edges)
        add_list_to_result_string(self.geo_nodes)
        add_list_to_result_string(self.geo_points)
        add_list_to_result_string(self.signals)
        add_list_to_result_string(self.control_elements)
        add_list_to_result_string(self.trips)
        add_list_to_result_string(self.top_edges)
        add_list_to_result_string(self.top_nodes)

        result_string = result_string + RootXML.get_accommodation_xml()
        result_string = result_string + RootXML.get_suffix(self.uuids, self.config)

        if filename is None:
            return result_string

        with open(f"{filename}.ppxml", "w") as out:
            out.write(result_string)

    def generate_nodes(self, nodes):
        for node in nodes:
            self.uuids = self.uuids + node.get_uuids() + node.geo_node.get_uuids()
            self.geo_nodes.append(NodeXML.get_geo_node_xml(node.geo_node, node.identifier))
            self.geo_points.append(NodeXML.get_geo_point_xml(node.geo_node, node.identifier, self.config))
            self.top_nodes.append(NodeXML.get_top_node_xml(node))

    def generate_edges(self, edges):
        for edge in edges:
            self.uuids = self.uuids + edge.get_uuids()
            self.top_edges.append(EdgeXML.get_top_edge_xml(edge))
            all_geo_nodes = [edge.node_a.geo_node] + edge.intermediate_geo_nodes + [edge.node_b.geo_node]
            for i in range(len(all_geo_nodes)-1):
                node_a = all_geo_nodes[i]
                node_b = all_geo_nodes[i + 1]
                geo_edge_uuid = str(uuid.uuid4())
                self.uuids.append(geo_edge_uuid)
                self.geo_edges.append(EdgeXML.get_geo_edge_xml(node_a, node_b, geo_edge_uuid, edge))

            edge_identifier = f"{edge.node_a.identifier} to {edge.node_b.identifier}"
            for intermediate_geo_node in edge.intermediate_geo_nodes:
                self.uuids = self.uuids + intermediate_geo_node.get_uuids()
                self.geo_nodes.append(NodeXML.get_geo_node_xml(intermediate_geo_node, edge_identifier))
                self.geo_points.append(NodeXML.get_geo_point_xml(intermediate_geo_node, edge_identifier))

    def generate_signals(self, signals):
        for signal in signals:
            self.uuids = self.uuids + signal.get_uuids()
            self.control_elements.append(SignalXML.get_control_memeber_xml(signal))
            self.signals.append(SignalXML.get_signal_xml(signal))

    def generate_trips(self, trips):
        for trip in trips:
            self.uuids.append(trip.trip_uuid)
            self.trips.append(TripXML.get_trip_xml(trip))

    def generate_routes(self, routes):
        for route in routes:
            if route.end_signal is None:
                continue
            self.uuids.append(route.route_uuid)
            self.routes.append(RouteXML.get_route_xml(route))
