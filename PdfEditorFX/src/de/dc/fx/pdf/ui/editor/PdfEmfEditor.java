package de.dc.fx.pdf.ui.editor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import de.dc.workbench.fx.ui.SimpleEmfEditor;
import de.dc.workbench.fx.ui.pdfx.FXPdfDocument;
import de.dc.workbench.fx.ui.pdfx.PdfxFactory;
import de.dc.workbench.fx.ui.pdfx.PdfxPackage;
import de.dc.workbench.fx.ui.pdfx.provider.PdfxItemProviderAdapterFactory;

public class PdfEmfEditor extends SimpleEmfEditor<FXPdfDocument>{

	@Override
	protected AdapterFactory getModelItemProviderAdapterFactory() {
		return new PdfxItemProviderAdapterFactory();
	}

	@Override
	protected EObject createRootModel() {
		return PdfxFactory.eINSTANCE.createFXPdfDocument();
	}

	@Override
	protected EPackage getEPackage() {
		return PdfxPackage.eINSTANCE;
	}

}
