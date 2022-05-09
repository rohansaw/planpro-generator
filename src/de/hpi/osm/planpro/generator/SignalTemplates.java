package de.hpi.osm.planpro.generator;

public class SignalTemplates {

	public static String getControllMemberTemplate() {
		// @formatter:off
		return "            <Stellelement>"+"\n"
				+"              <Identitaet>"+"\n"
				+"                <Wert>%s</Wert>"+"\n"
				+"              </Identitaet>"+"\n"
				+"              <Basis_Objekt_Allg>"+"\n"
				+"                <Datum_Regelwerk>"+"\n"
				+"                  <Wert>2012-02-24</Wert>"+"\n"
				+"                </Datum_Regelwerk>"+"\n"
				+"              </Basis_Objekt_Allg>"+"\n"
				+"              <Objektreferenzen/>"+"\n"
				+"              <ID_Energie>"+"\n"
				+"                <Wert>C35FDB88-889E-539E-A68B-6079265F70D9</Wert>"+"\n"
				+"              </ID_Energie>"+"\n"
				+"              <ID_Information>"+"\n"
				+"                <Wert>C35FDB88-889E-539E-A68B-6079265F70D9</Wert>"+"\n"
				+"              </ID_Information>"+"\n"
				+"            </Stellelement>"+"\n";
		// @formatter:on
	}

	public static String getSignalTemplate() {
		// @formatter:off
		return "            <Signal>"+"\n"
				+"              <Identitaet>"+"\n"
				+"                <Wert>%s</Wert>"+"\n"
				+"              </Identitaet>"+"\n"
				+"              <Basis_Objekt_Allg>"+"\n"
				+"                <Datum_Regelwerk>"+"\n"
				+"                  <Wert>2012-02-24</Wert>"+"\n"
				+"                </Datum_Regelwerk>"+"\n"
				+"              </Basis_Objekt_Allg>"+"\n"
				+"              <Objektreferenzen/>"+"\n"
				+"              <Punkt_Objekt_Strecke>"+"\n"
				+"                <ID_Strecke>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </ID_Strecke>"+"\n"
				+"                <Strecke_Km>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Strecke_Km>"+"\n"
				+"              </Punkt_Objekt_Strecke>"+"\n"
				+"              <Punkt_Objekt_TOP_Kante>"+"\n"
				+"                <Abstand>"+"\n"
				+"                  <Wert>%.3f</Wert>"+"\n"
				+"                </Abstand>"+"\n"
				+"                <ID_TOP_Kante>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </ID_TOP_Kante>"+"\n"
				+"                <Wirkrichtung>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Wirkrichtung>"+"\n"
				+"                <Seitlicher_Abstand>"+"\n"
				+"                  <Wert>%.3f</Wert>"+"\n"
				+"                </Seitlicher_Abstand>"+"\n"
				+"              </Punkt_Objekt_TOP_Kante>"+"\n"
				+"              <Bezeichnung>"+"\n"
				+"                <Bezeichnung_Aussenanlage>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Bezeichnung_Aussenanlage>"+"\n"
				+"                <Bezeichnung_Lageplan_Kurz>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Bezeichnung_Lageplan_Kurz>"+"\n"
				+"                <Bezeichnung_Lageplan_Lang>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Bezeichnung_Lageplan_Lang>"+"\n"
				+"                <Bezeichnung_Tabelle>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Bezeichnung_Tabelle>"+"\n"
				+"                <Kennzahl>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Kennzahl>"+"\n"
				+"                <Oertlicher_Elementname>"+"\n"
				+"                  <Wert>%s</Wert>"+"\n"
				+"                </Oertlicher_Elementname>"+"\n"
				+"              </Bezeichnung>"+"\n"
				+"              <Signal_Real>"+"\n"
				+"                <Signal_Befestigungsart>"+"\n"
				+"                  <Wert>Mast</Wert>"+"\n"
				+"                </Signal_Befestigungsart>"+"\n"
				+"                <Signal_Real_Aktiv>"+"\n"
				+"                  <ID_Stellelement>"+"\n"
				+"                    <Wert>%s</Wert>"+"\n"
				+"                  </ID_Stellelement>"+"\n"
				+"                  <Signal_Funktion>"+"\n"
				+"                    <Wert>%s</Wert>"+"\n"
				+"                  </Signal_Funktion>"+"\n"
				+"                </Signal_Real_Aktiv>"+"\n"
				+"                <Signal_Real_Aktiv_Schirm>"+"\n"
				+"                  <Dunkelschaltung>"+"\n"
				+"                    <Wert>false</Wert>"+"\n"
				+"                  </Dunkelschaltung>"+"\n"
				+"                  <Richtpunktentfernung>"+"\n"
				+"                    <Wert>150</Wert>"+"\n"
				+"                  </Richtpunktentfernung>"+"\n"
				+"                  <Signal_Art>"+"\n"
				+"                    <Wert>%s</Wert>"+"\n"
				+"                  </Signal_Art>"+"\n"
				+"                  <Signalsystem>"+"\n"
				+"                    <Wert>Ks</Wert>"+"\n"
				+"                  </Signalsystem>"+"\n"
				+"                  <Streuscheibe_Art>"+"\n"
				+"                    <Wert>HG</Wert>"+"\n"
				+"                  </Streuscheibe_Art>"+"\n"
				+"                  <Streuscheibe_Betriebsstellung>"+"\n"
				+"                    <Wert>HG4</Wert>"+"\n"
				+"                  </Streuscheibe_Betriebsstellung>"+"\n"
				+"                </Signal_Real_Aktiv_Schirm>"+"\n"
				+"              </Signal_Real>"+"\n"
				+"            </Signal>"+"\n";
		// @formatter:on
	}

}
