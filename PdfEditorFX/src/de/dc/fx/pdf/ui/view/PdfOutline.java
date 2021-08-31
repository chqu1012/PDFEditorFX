package de.dc.fx.pdf.ui.view;

import java.io.IOException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.dc.fx.pdf.ui.editor.PdfEditor;
import de.dc.workbench.fx.core.command.di.EmfFXPlatform;
import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.EventTopic;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.ui.EmfFilteredTreeView;
import de.dc.workbench.fx.ui.pdfx.FXOutlineItem;
import de.dc.workbench.fx.ui.pdfx.PdfxFactory;
import de.dc.workbench.fx.ui.pdfx.PdfxPackage;
import de.dc.workbench.fx.ui.pdfx.provider.FXOutlineItemItemProvider;
import de.dc.workbench.fx.ui.pdfx.provider.PdfxItemProviderAdapterFactory;
import de.dc.workbench.fx.ui.pdfx.service.IPdfxService;
import de.dc.workbench.fx.ui.views.pdf.PdfView;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

public class PdfOutline extends EmfFilteredTreeView<FXOutlineItem>{

	@Inject IPdfxService pdfxService;
	@Inject IEventBroker eventBroker;
	
	public PdfOutline() {
		super("Outline", "/de/dc/workbench/fx/ui/pdfx/icons/outline.png");
		
		EmfFXPlatform.inject(this);
		eventBroker.register(this);
		treeView.setShowRoot(false);
	}

	@Subscribe
	public void subscribeOpenPdfFile(EventContext<Tab> context) {
		if (context.match(EventTopic.CURRENT_SELECTED_EDITOR)) {
			var input = context.getInput();
			if (input instanceof PdfEditor editor) {
					Thread thread = new Thread(() -> {
						try {
							var outline = pdfxService.findOutline(editor.getFile());
							if(outline==null) {
								return;
							}
							Platform.runLater(() -> {
								setInput(outline.getItems().get(0));
								expandAll();										
							});
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					thread.setDaemon(true);
					thread.start();
			}
		}
	}
	
	@Override
	protected void onTreeViewSelectedItemChanged(ObservableValue<? extends TreeItem<FXOutlineItem>> observable,
			TreeItem<FXOutlineItem> oldValue, TreeItem<FXOutlineItem> newValue) {
		super.onTreeViewSelectedItemChanged(observable, oldValue, newValue);
		if (newValue!=null) {
			FXOutlineItem item = newValue.getValue();
			eventBroker.post(PdfView.TOPIC_OPEN_PAGE_BY_NUMBER, item.getPageNumber());
		}
	}
	
	@Override
	protected AdapterFactory getModelItemProviderAdapterFactory() {
		return new PdfxItemProviderAdapterFactory() {
			@Override
			public Adapter createFXOutlineItemAdapter() {
				if (fxOutlineItemItemProvider ==null) {
					fxOutlineItemItemProvider = new FXOutlineItemItemProvider(this) {
						@Override
						public String getText(Object object) {
							if (object instanceof FXOutlineItem item) {
								return item.getTitle();
							}
							return super.getText(object);
						}
					};
				}
				return super.createFXOutlineItemAdapter();
			}
		};
	}

	@Override
	protected EObject createRootModel() {
		return PdfxFactory.eINSTANCE.createFXOutline();
	}

	@Override
	protected EPackage getEPackage() {
		return PdfxPackage.eINSTANCE;
	}

}
