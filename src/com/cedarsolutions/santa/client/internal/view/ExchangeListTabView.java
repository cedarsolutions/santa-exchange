/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Apache License, Version 2.0.
 * See LICENSE for more information about the licensing terms.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
 * Language : Java 6
 * Project  : Secret Santa Exchange
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.santa.client.internal.view;

import static com.cedarsolutions.client.gwt.event.UnifiedEventType.CLICK_EVENT;

import java.util.List;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.handler.AbstractClickHandler;
import com.cedarsolutions.client.gwt.handler.AbstractEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.ModuleTabView;
import com.cedarsolutions.client.gwt.widget.DataTable;
import com.cedarsolutions.client.gwt.widget.DataTablePager;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotSupportedException;
import com.cedarsolutions.santa.client.common.widget.ConfirmationPopup;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeKeyProvider;
import com.cedarsolutions.web.metadata.NativeEventType;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.HasData;
import com.google.inject.Singleton;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Exchange list tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Singleton
public class ExchangeListTabView extends ModuleTabView implements IExchangeListTabView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, ExchangeListTabView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<ExchangeListTabView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Label paragraph1;
    @UiField @WithElementId protected Button createButton;
    @UiField @WithElementId protected Button deleteButton;
    @UiField(provided = true) @WithElementId protected DataTable<Exchange> table;
    @UiField(provided = true) @WithElementId protected DataTablePager pager;

    // Other instance variables
    protected ConfirmationPopup deletePopup;
    private ViewEventHandler refreshEventHandler;
    private ViewEventHandler criteriaResetEventHandler;
    private ViewEventHandler createHandler;
    private ViewEventHandler deleteHandler;
    private ViewEventHandlerWithContext<Exchange> editExchangeHandler;
    private ExchangeCriteria criteria;

    /** Create the view. */
    public ExchangeListTabView() {
        this.createProvidedWidgets();  // do this before binder, otherwise we get NPE
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.setupBinderWidgets();
    }

    /** Get the refresh event handler. */
    @Override
    public ViewEventHandler getRefreshEventHandler() {
        return this.refreshEventHandler;
    }

    /** Set the refresh event handler. */
    @Override
    public void setRefreshEventHandler(ViewEventHandler refreshEventHandler) {
        this.refreshEventHandler = refreshEventHandler;
    }

    /** Get the criteria reset event handler. */
    @Override
    public ViewEventHandler getCriteriaResetEventHandler() {
        return this.criteriaResetEventHandler;
    }

    /** Set the criteria reset event handler. */
    @Override
    public void setCriteriaResetEventHandler(ViewEventHandler criteriaResetEventHandler) {
        this.criteriaResetEventHandler = criteriaResetEventHandler;
    }

    /** Get the delete handler. */
    @Override
    public ViewEventHandler getDeleteHandler() {
        return this.deleteHandler;
    }

    /** Set the delete handler. */
    @Override
    public void setDeleteHandler(ViewEventHandler deleteHandler) {
        this.deleteHandler = deleteHandler;
    }

    /** Get the create handler. */
    @Override
    public ViewEventHandler getCreateHandler() {
        return this.createHandler;
    }

    /** Set the create handler. */
    @Override
    public void setCreateHandler(ViewEventHandler createHandler) {
        this.createHandler = createHandler;
    }

    /** Get the edit selected row handler. */
    @Override
    public ViewEventHandlerWithContext<Exchange> getEditSelectedRowHandler() {
        return this.editExchangeHandler;
    }

    /** Set the edit selected row handler. */
    @Override
    public void setEditSelectedRowHandler(ViewEventHandlerWithContext<Exchange> editExchangeHandler) {
        this.editExchangeHandler = editExchangeHandler;
    }

    /** Show a validation error related to search criteria. */
    @Override
    public void showValidationError(InvalidDataException error) {
        throw new NotSupportedException("Validation is not supported by this view");
    }

    /** Get the number of rows per page. */
    @Override
    public int getPageSize() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        return constants.exchangeList_pageSize();
    }

    /** Get the underlying table for this renderer. */
    protected DataTable<Exchange> getTable() {
        return this.table;
    }

    /** Get the display for this renderer. */
    @Override
    public HasData<Exchange> getDisplay() {
        return this.table;
    }

    /** Get the selected records. */
    @Override
    public List<Exchange> getSelectedRecords() {
        return this.table.getSelectedRecords();
    }

    /** Whether this renderer already has criteria set. */
    @Override
    public boolean hasSearchCriteria() {
        return this.criteria != null;
    }

    /** Set the search criteria for this renderer. */
    @Override
    public void setSearchCriteria(ExchangeCriteria criteria) {
        this.criteria = criteria;
    }

    /** Get the search criteria for this renderer. */
    @Override
    public ExchangeCriteria getSearchCriteria() {
        return this.criteria;
    }

    /** Get the delete confirmation popup. */
    protected ConfirmationPopup getDeletePopup() {
        return this.deletePopup;
    }

    /** Create and set up the provided widgets that aren't controlled by the UI binder. */
    private void createProvidedWidgets() {
        this.deletePopup = createDeletePopup();
        this.setupTable();
    }

    /** Set up the widgets that are controlled by the UI binder. */
    private void setupBinderWidgets() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        this.paragraph1.setText(constants.exchangeList_paragraph1());

        this.createButton.setText(constants.exchangeList_createButton());
        this.createButton.addClickHandler(new CreateClickHandler(this));

        this.deleteButton.setText(constants.exchangeList_deleteButton());
        this.deleteButton.addClickHandler(new DeleteClickHandler(this));
    }

    /** Setup the displayed table. */
    private void setupTable() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        ExchangeKeyProvider keyProvider = new ExchangeKeyProvider();
        this.table = new DataTable<Exchange>(constants.exchangeList_pageSize(), "675px", keyProvider);
        this.pager = table.getPager();

        this.table.setNoRowsMessage(constants.exchangeList_noRows());
        table.addCellPreviewHandler(new RowClickHandler(this));

        this.table.addSelectionColumn(10, Unit.PCT, keyProvider);
        addColumn(new NameColumn(), constants.exchangeList_nameTitle(), 60);
        addColumn(new StateColumn(), constants.exchangeList_stateTitle(), 40);
    }

    /** Add a column to the table. */
    private void addColumn(Column<Exchange, ?> column, String title, int width) {
        this.table.addColumn(column, title);
        this.table.setColumnWidth(column, width, Unit.PCT);
    }

    /**
     * Create a delete confirmation popup.
     * @param items Number of items being deleted
     */
    protected ConfirmationPopup createDeletePopup() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        String title = constants.exchangeList_confirmDeleteTitle();
        ConfirmationPopup popup = new ConfirmationPopup(title);
        popup.setOkButtonHandler(new DeleteConfirmHandler(this));
        return popup;
    }

    /** Show the delete confirmation popup. */
    protected void showDeletePopup(int deletedItems) {
        InternalConstants constants = GWT.create(InternalConstants.class);
        String message = deletedItems == 1 ? constants.exchangeList_confirmDeleteMessageOne() : constants.exchangeList_confirmDeleteMessageSeveral();
        this.deletePopup.setMessage(message);
        this.deletePopup.showPopup();
    }

    /** Name column. */
    protected static class NameColumn extends Column<Exchange, String> {
        public NameColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(Exchange item) {
            return item == null || item.getName() == null ? "" : item.getName();
        }
    }

    /** State column. */
    protected static class StateColumn extends Column<Exchange, String> {
        public StateColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(Exchange item) {
            if (item == null || item.getExchangeState() == null) {
                return "";
            } else {
                return InternalUtils.getExchangeStateForDisplay(item.getExchangeState());
            }
        }
    }

    /** Create click handler. */
    protected static class CreateClickHandler extends AbstractClickHandler<ExchangeListTabView> {
        public CreateClickHandler(ExchangeListTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getCreateHandler();
        }
    }

    /** Delete click handler. */
    protected static class DeleteClickHandler extends AbstractEventHandler<ExchangeListTabView> implements ClickHandler {
        public DeleteClickHandler(ExchangeListTabView parent) {
            super(parent);
        }

        @Override
        public void onClick(ClickEvent event) {
            // Only bother to show confirmation if the parent will actually do something
            if (this.getParent().getDeleteHandler() != null) {
                List<Exchange> selected = this.getParent().getTable().getSelectedRecords();
                if (selected != null && !selected.isEmpty()) {
                    this.getParent().showDeletePopup(selected.size());
                    // later, the DeleteConfirmHandler will actually invoke the parent's delete event handler
                }
            }
        }
    }

    /** Delete confirm handler. */
    protected static class DeleteConfirmHandler extends AbstractViewEventHandler<ExchangeListTabView> {
        public DeleteConfirmHandler(ExchangeListTabView handler) {
            super(handler);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            if (this.getParent().getDeleteHandler() != null) {
                this.getParent().getDeleteHandler().handleEvent(event);
            }
        }
    }

    /** Row click handler. */
    protected static class RowClickHandler extends AbstractEventHandler<ExchangeListTabView> implements CellPreviewEvent.Handler<Exchange> {
        public RowClickHandler(ExchangeListTabView parent) {
            super(parent);
        }

        @Override
        public void onCellPreview(CellPreviewEvent<Exchange> event) {
            this.handleSelectedRow(event.getNativeEvent().getType(), event.getColumn(), event.getValue());
        }

        protected void handleSelectedRow(String event, int column, Exchange row) {
            if (this.getParent().getEditSelectedRowHandler() != null) {
                if (NativeEventType.CLICK.equals(NativeEventType.convert(event))) {
                    if (column > 0 && row != null) {  // Column zero is the selection column
                        UnifiedEventWithContext<Exchange> context = new UnifiedEventWithContext<Exchange>(CLICK_EVENT, row);
                        this.getParent().getEditSelectedRowHandler().handleEvent(context);
                    }
                }
            }
        }
    }
}
