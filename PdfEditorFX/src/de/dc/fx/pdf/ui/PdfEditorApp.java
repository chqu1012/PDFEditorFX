package de.dc.fx.pdf.ui;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.workspace.di.WorkspaceModule;
import de.dc.workbench.fx.core.xtext.IXtextService;
import de.dc.workbench.fx.core.xtext.di.XtextModule;
import de.dc.workbench.fx.ui.EmfApplication;
import de.dc.workbench.fx.ui.monaco.di.MonacoModule;
import de.dc.workbench.fx.ui.pdfx.di.PdfxModule;

public class PdfEditorApp extends EmfApplication{

	public static final String ID = "Pdfx";
	
	@Inject IXtextService xtextService;
	
	@Override
	protected boolean isStandaloneApp() {
		return true;
	}
	
	@Override
	protected void addModules() {
		addModule(new WorkspaceModule());
		addModule(new XtextModule());
		addModule(new MonacoModule());
		addModule(new PdfxModule());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
