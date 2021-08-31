package de.dc.fx.pdf.ui.file;

import de.dc.workbench.fx.ui.pdfx.FXOutline;
import de.dc.workbench.fx.ui.pdfx.PdfxFactory;
import de.dc.workbench.fx.ui.pdfx.PdfxPackage;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;

import de.dc.workbench.fx.core.file.EmfFile;

public class OutlineFile extends EmfFile<FXOutline>{

	@Override
	public EPackage getEPackageEInstance() {
		return PdfxPackage.eINSTANCE;
	}

	@Override
	public EFactory getEFactoryEInstance() {
		return PdfxFactory.eINSTANCE;
	}

}
