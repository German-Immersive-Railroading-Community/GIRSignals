package eu.gir.girsignals.blocks;

import eu.gir.girsignals.EnumSignals.RA;
import eu.gir.girsignals.EnumSignals.RA_LIGHT;
import eu.gir.girsignals.SEProperty;
import eu.gir.girsignals.SEProperty.ChangeableStage;
import eu.gir.girsignals.init.GIRItems;

public class SignalRA extends Signal {

	public SignalRA() {
		super(GIRItems.SIGN_PLACEMENT_TOOL, "rasignal", 0);
	}

	public static final SEProperty<RA> RATYPE = SEProperty.of("ratype", RA.RA10, ChangeableStage.GUISTAGE);
	public static final SEProperty<RA_LIGHT> RALIGHT = SEProperty.of("ralight", RA_LIGHT.OFF);

}
