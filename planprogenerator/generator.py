from .planproxml import NodeXML, EdgeXML, SignalXML, RouteXML, RootXML, TripXML
from .model import Trip


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

    def generate(self, nodes, edges, signals, filename):
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

        # Create Trip
        trip = Trip(edges)
        for signal in signals:
            signal.trip = trip

        self.uuids = self.uuids + RootXML.get_root_uuids()

        self.generate_nodes(nodes)
        self.generate_edges(edges)
        self.generate_signals(signals)
        self.generate_trips([trip])
        self.generate_routes([])

        with open(f"{filename}.ppxml", "w") as out:
            out.write(RootXML.get_prefix_xml())
            out.write(RootXML.get_external_element_control_xml())

            def write_list(_list, _out):
                for entry in _list:
                    _out.write(entry)

            write_list(self.routes, out)
            write_list(self.geo_edges, out)
            write_list(self.geo_nodes, out)
            write_list(self.geo_points, out)
            write_list(self.signals, out)
            write_list(self.control_elements, out)
            write_list(self.trips, out)
            write_list(self.top_edges, out)
            write_list(self.top_nodes, out)

            out.write(RootXML.get_accommodation_xml())
            out.write(RootXML.get_suffix(self.uuids))

    def generate_nodes(self, nodes):
        for node in nodes:
            self.uuids = self.uuids + node.get_uuids()
            self.geo_nodes.append(NodeXML.get_geo_node_xml(node))
            self.geo_points.append(NodeXML.get_geo_point_xml(node))
            self.top_nodes.append(NodeXML.get_top_node_xml(node))

    def generate_edges(self, edges):
        for edge in edges:
            self.uuids = self.uuids + edge.get_uuids()
            self.top_edges.append(EdgeXML.get_top_edge_xml(edge))
            self.geo_edges.append(EdgeXML.get_geo_edge_xml(edge))

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