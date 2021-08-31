package de.dc.fx.pdf.ui.editor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.google.inject.Inject;

import de.dc.workbench.fx.core.IEditor;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.ui.pdfx.UIConstants;
import de.dc.workbench.fx.ui.pdfx.model.SearchModel;
import de.dc.workbench.fx.ui.views.pdf.PdfView;
import de.dc.workbench.fx.ui.views.pdf.stripper.MainSource;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PdfEditor extends PdfView implements IEditor<String>{

	@Inject IEventBroker eventBroker;
	
	private File file;
	private PDDocument document;

	public PdfEditor() {
		setText("Pdf Editor");
		var image = new Image(getClass().getResourceAsStream("/de/dc/workbench/fx/ui/pdfx/icons/pdf.png"));
		var imageView = new ImageView(image);
		imageView.setFitHeight(24);
		imageView.setFitWidth(24);
		setGraphic(imageView);
		setOnClosed(e->{
			try {
				document.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}
	
	@Override
	protected void onButtonAction(ActionEvent event) {
		super.onButtonAction(event);
		
		try {
			MainSource stripper = new MainSource();
			stripper.setSortByPosition(true);

			stripper.setStartPage(0);
			stripper.setEndPage(document.getNumberOfPages());

			Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
			stripper.find(document, "Eclipse");
			stripper.writeText(document, dummy);
			
			renderer = new PDFRenderer(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		eventBroker.post(UIConstants.SEARCH_PDF_BY_STRING, new SearchModel(document, "Eclipse"));
	}
	
	@Override
	public void save() {
	}

	@Override
	public void save(File file) {
		
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String load(String filePath) {
		this.file = new File(filePath);
		setText(file.getName());
		document = loadPdf(filePath);
		return "";
	}

	@Override
	public String load(File file) {
		return load(file.getAbsolutePath());
	}

	@Override
	public String getExtension() {
		return "pdf";
	}

	@Override
	public void createMenu(String name, String commandId, String icon) {
	}

	@Override
	public MenuItem findMenuItem(String commandId) {
		return null;
	}

	@Override
	public void setDirty(boolean b) {
	}

}
