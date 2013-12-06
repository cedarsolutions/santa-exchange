/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.admin.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cedarsolutions.client.gwt.custom.datepicker.DateBox;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventClickHandler;
import com.cedarsolutions.client.gwt.module.view.ModuleTabView;
import com.cedarsolutions.client.gwt.validation.IValidationErrorWidget;
import com.cedarsolutions.client.gwt.validation.ValidationUtils;
import com.cedarsolutions.client.gwt.widget.table.ColumnWithTooltip;
import com.cedarsolutions.client.gwt.widget.table.DataTable;
import com.cedarsolutions.client.gwt.widget.table.DataTablePager;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.santa.client.common.widget.AuditEventTypeList;
import com.cedarsolutions.santa.client.common.widget.SantaExchangeStyles;
import com.cedarsolutions.santa.client.common.widget.ValidationErrorWidget;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventKeyProvider;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.cedarsolutions.util.gwt.GwtLocalizationUtils;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.cedarsolutions.web.metadata.NativeEventType;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Singleton;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Tab that contains audit functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Singleton
public class AuditTabView extends ModuleTabView implements IAuditTabView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, AuditTabView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<AuditTabView> {
    }

    // User interface fields fron the UI binder
    @UiField protected ValidationErrorWidget validationErrorWidget;
    @UiField @WithElementId protected Label eventTypeLabel;
    @UiField @WithElementId protected Label userIdLabel;
    @UiField @WithElementId protected TextBox userIdInput;
    @UiField @WithElementId protected Label eventDateLabel;
    @UiField @WithElementId protected Label toLabel;
    @UiField @WithElementId protected Button refreshButton;
    @UiField @WithElementId protected Button clearButton;
    @UiField @WithElementId protected DisclosurePanel filterDisclosure;

    // Provided UI widgets
    @UiField(provided = true) @WithElementId protected AuditEventTypeList eventTypeInput;
    @UiField(provided = true) @WithElementId protected DateBox startDateInput;
    @UiField(provided = true) @WithElementId protected DateBox endDateInput;
    @UiField(provided = true) @WithElementId protected DataTable<AuditEvent> table;
    @UiField(provided = true) @WithElementId protected DataTablePager pager;

    // Other instance variables
    private String historyToken;
    private boolean hasSearchCriteria;
    private ViewEventHandler refreshEventHandler;
    private ViewEventHandler criteriaResetEventHandler;
    private ViewEventHandlerWithContext<String> userIdSelectedHandler;

    /** Create the view. */
    public AuditTabView() {
        this.createProvidedWidgets();  // do this before binder, otherwise we get NPE
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.setupBinderWidgets();
    }

    /** Set the history token. */
    @Override
    public void setHistoryToken(String historyToken) {
        this.historyToken = historyToken;
    }

    /** Get the history token for this tab. */
    @Override
    public String getHistoryToken() {
        return this.historyToken;
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

    /** Get the user id selected handler. */
    @Override
    public ViewEventHandlerWithContext<String> getUserIdSelectedHandler() {
        return this.userIdSelectedHandler;
    }

    /** Set the user id selected handler. */
    @Override
    public void setUserIdSelectedHandler(ViewEventHandlerWithContext<String> userIdSelectedHandler) {
        this.userIdSelectedHandler = userIdSelectedHandler;
    }

    /** Get the validation error widget. */
    @Override
    public IValidationErrorWidget getValidationErrorWidget() {
        return this.validationErrorWidget;
    }

    /** Show a validation error related to search criteria. */
    @Override
    public void showValidationError(InvalidDataException error) {
        ViewUtils.getInstance().showValidationError(this, error);
    }

    /** Translate a localizable message using this view's resources. */
    @Override
    public String translate(LocalizableMessage message) {
        AdminMessageConstants constants = GWT.create(AdminMessageConstants.class);
        return GwtLocalizationUtils.translate(message, constants, "auditTab");
    }

    /** Get a mapping from field name to input element. */
    @Override
    public Map<String, UIObject> getValidatedFieldMap() {
        return ValidationUtils.NO_SPECIFIC_FIELDS;
    }

    /** Get the number of rows per page. */
    @Override
    public int getPageSize() {
        AdminConstants constants = GWT.create(AdminConstants.class);
        return constants.auditTab_pageSize();
    }

    /** Get the display for this renderer. */
    @Override
    public HasData<AuditEvent> getDisplay() {
        return this.table;
    }

    /** Whether this renderer already has criteria set. */
    @Override
    public boolean hasSearchCriteria() {
        return this.hasSearchCriteria;
    }

    /** Set the search criteria for this renderer. */
    @Override
    public void setSearchCriteria(AuditEventCriteria criteria) {
        this.hasSearchCriteria = true;

        if (criteria.getEventTypes() == null || criteria.getEventTypes().isEmpty()) {
            this.eventTypeInput.setSelectedValue(null);
        } else {
            this.eventTypeInput.setSelectedValue(criteria.getEventTypes().get(0));
        }

        if (criteria.getUserIds() == null || criteria.getUserIds().isEmpty()) {
            this.userIdInput.setText("");
        } else {
            this.userIdInput.setText(criteria.getUserIds().get(0));
        }

        this.startDateInput.setValue(criteria.getStartDate());
        this.endDateInput.setValue(criteria.getEndDate());
    }

    /** Get the search criteria for this renderer. */
    @Override
    public AuditEventCriteria getSearchCriteria() {
        ViewUtils.getInstance().clearValidationErrors(this);

        AuditEventCriteria criteria = new AuditEventCriteria();

        if (this.eventTypeInput.getSelectedValue() != null) {
            criteria.setEventTypes(this.eventTypeInput.getSelectedValue());
        }

        if (!GwtStringUtils.isEmpty(this.userIdInput.getText())) {
            criteria.setUserIds(this.userIdInput.getText());
        }

        // Dates are supposed to be inclusive, so go from start at 00:00 to end at 23:59
        Date startDate = GwtDateUtils.resetTime(this.startDateInput.getDatePicker().getValue(), "00:00");
        Date endDate = GwtDateUtils.resetTime(this.endDateInput.getDatePicker().getValue(), "23:59");
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);

        return criteria;
    }

    /** Create and set up the provided widgets that aren't controlled by the UI binder. */
    private void createProvidedWidgets() {
        this.eventTypeInput = new AuditEventTypeList(true);

        this.startDateInput = new DateBox();
        this.startDateInput.setFormat(new DateBox.DefaultFormat(GwtDateUtils.getDateFormat()));

        this.endDateInput = new DateBox();
        this.endDateInput.setFormat(new DateBox.DefaultFormat(GwtDateUtils.getDateFormat()));

        this.setupTable();
    }

    /** Set up the widgets that are controlled by the UI binder. */
    private void setupBinderWidgets() {
        AdminConstants constants = GWT.create(AdminConstants.class);

        this.filterDisclosure.getHeaderTextAccessor().setText(constants.auditTab_filterResults());
        this.eventTypeLabel.setText(constants.auditTab_eventTypeCriteria());
        this.userIdLabel.setText(constants.auditTab_userIdCriteria());
        this.eventDateLabel.setText(constants.auditTab_eventDateCriteria());
        this.toLabel.setText(constants.auditTab_to());

        this.refreshButton.setText(constants.auditTab_refreshButton());
        this.refreshButton.addClickHandler(new RefreshClickHandler(this));

        this.clearButton.setText(constants.auditTab_clearButton());
        this.clearButton.addClickHandler(new ClearClickHandler(this));
    }

    /** Setup the displayed table. */
    private void setupTable() {
        AdminConstants constants = GWT.create(AdminConstants.class);

        this.table = new DataTable<AuditEvent>(constants.auditTab_pageSize(), "100%", new AuditEventKeyProvider());
        this.pager = table.getPager();
        this.table.setNoRowsMessage(constants.auditTab_noRows());

        addColumn(new EventIdColumn(), constants.auditTab_eventIdTitle(), 10);
        addColumn(new EventTypeColumn(), constants.auditTab_eventTypeTitle(), 15);
        addColumn(new EventTimestampColumn(), constants.auditTab_eventTimestampTitle(), 20);
        addColumn(new UserIdColumn(this), constants.auditTab_userIdTitle(), 15);
        addColumn(new SessionIdColumn(), constants.auditTab_sessionIdTitle(), 15);
        addColumn(new ExtraDataColumn(), constants.auditTab_extraDataTitle(), 25);
    }

    /** Add a column to the table. */
    private void addColumn(Column<AuditEvent, ?> column, String title, int width) {
        table.addColumn(column, title);
        table.setColumnWidth(column, width, Unit.PCT);
    }

    /** Event id column. */
    protected static class EventIdColumn extends Column<AuditEvent, String> {
        public EventIdColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(AuditEvent item) {
            return item == null || item.getEventId() == null ? "" : String.valueOf(item.getEventId());
        }
    }

    /** Event type column. */
    protected static class EventTypeColumn extends ColumnWithTooltip<AuditEvent> {
        public EventTypeColumn() {
            super();
        }

        @Override
        public String getStringValue(AuditEvent item) {
            return item == null || item.getEventType() == null ? "" : item.getEventType().toString();
        }

        @Override
        public String getTooltip(AuditEvent item) {
            String value = this.getStringValue(item);
            AdminMessageConstants constants = GWT.create(AdminMessageConstants.class);
            return GwtLocalizationUtils.translate(constants, "auditEventTooltip", value, null);
        }
    }

    /** Event timestamp column. */
    protected static class EventTimestampColumn extends Column<AuditEvent, String> {
        public EventTimestampColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(AuditEvent item) {
            return item == null ? "" : GwtDateUtils.formatTimestamp(item.getEventTimestamp());
        }
    }

    /** User id column. */
    protected static class UserIdColumn extends Column<AuditEvent, String> {
        public UserIdColumn(AuditTabView parent) {
            super(new UserIdCell(parent));
            this.setSortable(false);
            this.setCellStyleNames(SantaExchangeStyles.TEXT_AS_LINK_STYLE);
        }

        @Override
        public String getValue(AuditEvent item) {
            return item == null || item.getUserId() == null ? "" : item.getUserId();
        }
    }

    /** Specialized cell for the user id value. */
    protected static class UserIdCell extends ClickableTextCell {
        protected AuditTabView parent;

        public UserIdCell(AuditTabView parent) {
            this.parent = parent;
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
            this.handleBrowserEvent(event.getType(), value);
        }

        protected void handleBrowserEvent(String event, String userId) {
            if (this.parent.getUserIdSelectedHandler() != null) {
                if (NativeEventType.CLICK.equals(NativeEventType.convert(event))) {
                    UnifiedEventWithContext<String> context = new UnifiedEventWithContext<String>(UnifiedEventType.CLICK_EVENT, userId);
                    this.parent.getUserIdSelectedHandler().handleEvent(context);
                }
            }
        }
    }

    /** Session id column. */
    protected static class SessionIdColumn extends Column<AuditEvent, String> {
        public SessionIdColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(AuditEvent item) {
            return item == null || item.getSessionId() == null ? "" : item.getSessionId();
        }
    }

    /** Extra data column. */
    protected static class ExtraDataColumn extends Column<AuditEvent, SafeHtml> {
        public ExtraDataColumn() {
            super(new SafeHtmlCell());
            this.setSortable(false);
        }

        @Override
        public SafeHtml getValue(AuditEvent item) {
            StringBuffer buffer = new StringBuffer();

            if (item != null) {
                List<ExtraData> extraData = item.getExtraData();
                if (extraData != null && !extraData.isEmpty()) {
                    for (ExtraData entry : extraData) {
                        String key = SafeHtmlUtils.htmlEscape(entry.getKey().toString());
                        String value = entry.getValue() == null ? "" : SafeHtmlUtils.htmlEscape(entry.getValue());
                        buffer.append(key);
                        buffer.append("=");
                        buffer.append(value);
                        buffer.append("<br/>");
                    }
                }
            }

            // This is safe because the contents have been escaped above
            return SafeHtmlUtils.fromTrustedString(buffer.toString());
        }
    }

    /** Refresh click handler. */
    protected static class RefreshClickHandler extends AbstractViewEventClickHandler<AuditTabView> {
        public RefreshClickHandler(AuditTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getRefreshEventHandler();
        }
    }

    /** Clear click handler. */
    protected static class ClearClickHandler extends AbstractViewEventClickHandler<AuditTabView> {
        public ClearClickHandler(AuditTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getCriteriaResetEventHandler();
        }
    }
}
