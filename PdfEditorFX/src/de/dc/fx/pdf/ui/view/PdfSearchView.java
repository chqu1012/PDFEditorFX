package de.dc.fx.pdf.ui.view;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.ui.EmfFilteredTreeView;
import de.dc.workbench.fx.ui.pdfx.FXSearchResult;
import de.dc.workbench.fx.ui.pdfx.PdfxFactory;
import de.dc.workbench.fx.ui.pdfx.PdfxPackage;
import de.dc.workbench.fx.ui.pdfx.SearchProvider;
import de.dc.workbench.fx.ui.pdfx.UIConstants;
import de.dc.workbench.fx.ui.pdfx.model.SearchModel;
import de.dc.workbench.fx.ui.pdfx.provider.PdfxItemProviderAdapterFactory;
import de.dc.workbench.fx.ui.pdfx.service.IPdfxService;
import de.dc.workbench.fx.ui.views.pdf.PdfView;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

public class PdfSearchView extends EmfFilteredTreeView<Object>{

	@Inject IPdfxService pdfService;
	@Inject IEventBroker eventBroker;
	
	public PdfSearchView() {
		super("Search", "/de/dc/workbench/fx/ui/pdfx/icons/search.png");
		eventBroker.register(this);
		treeView.setShowRoot(false);
	}
	
	@Subscribe
	public void subscribeSearchPdf(EventContext<SearchModel> context) {
		if (context.match(UIConstants.SEARCH_PDF_BY_STRING)) {
			SearchModel input = context.getInput();
			SearchProvider searchProvider = pdfService.getSearchProvider(input.getDocument(), input.getSearchString());
			setInput(searchProvider);
			expandAll();
		}
	}

	@Override
	protected void onTreeViewSelectedItemChanged(ObservableValue<? extends TreeItem<Object>> observable,
			TreeItem<Object> oldValue, TreeItem<Object> newValue) {
		super.onTreeViewSelectedItemChanged(observable, oldValue, newValue);
		if (newValue!=null) {
			Object value = newValue.getValue();
			if (value instanceof FXSearchResult result) {
				int pageNumber = result.getPageNumber();
				eventBroker.post(PdfView.TOPIC_OPEN_PAGE_BY_NUMBER, pageNumber);
			}
		}
	}
	
	@Override
	protected AdapterFactory getModelItemProviderAdapterFactory() {
		return new PdfxItemProviderAdapterFactory();
	}

	@Override
	protected EObject createRootModel() {
		return PdfxFactory.eINSTANCE.createSearchProvider();
	}

	@Override
	protected EPackage getEPackage() {
		return PdfxPackage.eINSTANCE;
	}

}
