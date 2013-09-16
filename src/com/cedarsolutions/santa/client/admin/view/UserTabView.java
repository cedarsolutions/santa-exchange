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

import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.ADMIN;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.LAST_LOGIN;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.LOCKED;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.LOGINS;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.OPEN_ID_PROVIDER;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.REGISTRATION_DATE;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.USER_ID;
import static com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn.USER_NAME;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cedarsolutions.client.gwt.custom.datepicker.DateBox;
import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractClickHandler;
import com.cedarsolutions.client.gwt.handler.AbstractColumnSortEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.ModuleTabView;
import com.cedarsolutions.client.gwt.validation.IValidationErrorWidget;
import com.cedarsolutions.client.gwt.validation.ValidationUtils;
import com.cedarsolutions.client.gwt.widget.ColumnWithName;
import com.cedarsolutions.client.gwt.widget.DataTablePager;
import com.cedarsolutions.client.gwt.widget.SortableTable;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.santa.client.common.widget.ConfirmationPopup;
import com.cedarsolutions.santa.client.common.widget.OpenIdProviderList;
import com.cedarsolutions.santa.client.common.widget.ValidationErrorWidget;
import com.cedarsolutions.santa.client.common.widget.YesNoList;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserKeyProvider;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.cedarsolutions.util.gwt.GwtLocalizationUtils;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
 * Tab that contains user maintenance functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Singleton
public class UserTabView extends ModuleTabView implements IUserTabView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, UserTabView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<UserTabView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Button deleteButton;
    @UiField @WithElementId protected Button lockButton;
    @UiField @WithElementId protected Button unlockButton;
    @UiField protected ValidationErrorWidget validationErrorWidget;
    @UiField @WithElementId protected Label userNameLabel;
    @UiField @WithElementId protected TextBox userNameInput;
    @UiField @WithElementId protected Label userIdLabel;
    @UiField @WithElementId protected TextBox userIdInput;
    @UiField @WithElementId protected Label providerLabel;
    @UiField @WithElementId protected Label adminLabel;
    @UiField @WithElementId protected Label lockedLabel;
    @UiField @WithElementId protected Label registrationDateLabel;
    @UiField @WithElementId protected Label toLabel;
    @UiField @WithElementId protected Button refreshButton;
    @UiField @WithElementId protected Button clearButton;
    @UiField @WithElementId protected DisclosurePanel filterDisclosure;

    // Provided UI widgets
    @UiField(provided = true) @WithElementId protected OpenIdProviderList providerInput;
    @UiField(provided = true) @WithElementId protected YesNoList adminInput;
    @UiField(provided = true) @WithElementId protected YesNoList lockedInput;
    @UiField(provided = true) @WithElementId protected DateBox startDateInput;
    @UiField(provided = true) @WithElementId protected DateBox endDateInput;
    @UiField(provided = true) @WithElementId protected SortableTable<RegisteredUser, RegisteredUserSortColumn> table;
    @UiField(provided = true) @WithElementId protected DataTablePager pager;

    // Other instance variables
    private String historyToken;
    private boolean hasSearchCriteria;
    private ViewEventHandler refreshEventHandler;
    private ViewEventHandler criteriaResetEventHandler;
    private ViewEventHandler deleteEventHandler;
    private ViewEventHandler lockEventHandler;
    private ViewEventHandler unlockEventHandler;
    private ConfirmationPopup deletePopup;

    /** Create the view. */
    public UserTabView() {
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

    /** Show the search criteria. */
    @Override
    public void showSearchCriteria() {
        this.filterDisclosure.setOpen(true);
    }

    /** Hide the search criteria. */
    @Override
    public void hideSearchCriteria() {
        this.filterDisclosure.setOpen(false);
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

    /** Get the delete event handler. */
    @Override
    public ViewEventHandler getDeleteEventHandler() {
        return this.deleteEventHandler;
    }

    /** Set the delete event handler. */
    @Override
    public void setDeleteEventHandler(ViewEventHandler deleteEventHandler) {
        this.deleteEventHandler = deleteEventHandler;
    }

    /** Get the lock event handler. */
    @Override
    public ViewEventHandler getLockEventHandler() {
        return this.lockEventHandler;
    }

    /** Set the lock event handler. */
    @Override
    public void setLockEventHandler(ViewEventHandler lockEventHandler) {
        this.lockEventHandler = lockEventHandler;
    }

    /** Get the unlock event handler. */
    @Override
    public ViewEventHandler getUnlockEventHandler() {
        return this.unlockEventHandler;
    }

    /** Set the unlock event handler. */
    @Override
    public void setUnlockEventHandler(ViewEventHandler unlockEventHandler) {
        this.unlockEventHandler = unlockEventHandler;
    }

    /** Get the number of rows per page. */
    @Override
    public int getPageSize() {
        AdminConstants constants = GWT.create(AdminConstants.class);
        return constants.userTab_pageSize();
    }

    /** Get the delete pop-up. */
    public ConfirmationPopup getDeletePopup() {
        return this.deletePopup;
    }

    /** Get the underlying table for this renderer. */
    protected SortableTable<RegisteredUser, RegisteredUserSortColumn> getTable() {
        return this.table;
    }

    /** Get the display for this renderer. */
    @Override
    public HasData<RegisteredUser> getDisplay() {
        return this.table;
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
        return GwtLocalizationUtils.translate(message, constants, "userTab");
    }

    /** Get a mapping from field name to input element. */
    @Override
    public Map<String, UIObject> getValidatedFieldMap() {
        return ValidationUtils.NO_SPECIFIC_FIELDS;
    }

    /** Get the selected records. */
    @Override
    public List<RegisteredUser> getSelectedRecords() {
        return this.table.getSelectedRecords();
    }

    /** Whether this renderer already has criteria set. */
    @Override
    public boolean hasSearchCriteria() {
        return this.hasSearchCriteria;
    }

    /** Set the search criteria for this renderer. */
    @Override
    public void setSearchCriteria(RegisteredUserCriteria criteria) {
        this.hasSearchCriteria = true;

        if (criteria.getUserNames() == null || criteria.getUserNames().isEmpty()) {
            this.userNameInput.setText("");
        } else {
            this.userNameInput.setText(criteria.getUserNames().get(0));
        }

        if (criteria.getUserIds() == null || criteria.getUserIds().isEmpty()) {
            this.userIdInput.setText("");
        } else {
            this.userIdInput.setText(criteria.getUserIds().get(0));
        }

        if (criteria.getOpenIdProviders() == null || criteria.getOpenIdProviders().isEmpty()) {
            this.providerInput.setSelectedValue(null);
        } else {
            this.providerInput.setSelectedValue(criteria.getOpenIdProviders().get(0));
        }

        this.adminInput.setSelectedValue(criteria.getAdmin());
        this.lockedInput.setSelectedValue(criteria.getLocked());

        this.startDateInput.setValue(criteria.getStartDate());
        this.endDateInput.setValue(criteria.getEndDate());

        this.table.setDisplaySortColumn(criteria);
    }

    /** Get the search criteria for this renderer. */
    @Override
    public RegisteredUserCriteria getSearchCriteria() {
        ViewUtils.getInstance().clearValidationErrors(this);

        RegisteredUserCriteria criteria = new RegisteredUserCriteria();

        if (!GwtStringUtils.isEmpty(this.userNameInput.getText())) {
            criteria.setUserNames(this.userNameInput.getText());
        }

        if (!GwtStringUtils.isEmpty(this.userIdInput.getText())) {
            criteria.setUserIds(this.userIdInput.getText());
        }

        if (this.providerInput.getSelectedValue() != null) {
            criteria.setOpenIdProviders(this.providerInput.getSelectedValue());
        }

        criteria.setAdmin(this.adminInput.getSelectedValue());
        criteria.setLocked(this.lockedInput.getSelectedValue());

        // Dates are supposed to be inclusive, so go from start at 00:00 to end at 23:59
        Date startDate = GwtDateUtils.resetTime(this.startDateInput.getDatePicker().getValue(), "00:00");
        Date endDate = GwtDateUtils.resetTime(this.endDateInput.getDatePicker().getValue(), "23:59");
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);

        this.table.setCriteriaSortColumn(criteria);

        return criteria;
    }

    /** Create and set up the provided widgets that aren't controlled by the UI binder. */
    private void createProvidedWidgets() {
        AdminConstants constants = GWT.create(AdminConstants.class);

        String title = constants.userTab_confirmDeleteTitle();
        String message = constants.userTab_confirmDeleteMessage();
        this.deletePopup = new ConfirmationPopup(title, message);
        this.deletePopup.setOkButtonHandler(new DeleteConfirmHandler(this));

        this.providerInput = new OpenIdProviderList(true);
        this.adminInput = new YesNoList(true);
        this.lockedInput = new YesNoList(true);

        this.startDateInput = new DateBox();
        this.startDateInput.setFormat(new DateBox.DefaultFormat(GwtDateUtils.getDateFormat()));

        this.endDateInput = new DateBox();
        this.endDateInput.setFormat(new DateBox.DefaultFormat(GwtDateUtils.getDateFormat()));

        this.setupTable();
    }

    /** Set up the widgets that are controlled by the UI binder. */
    private void setupBinderWidgets() {
        AdminConstants constants = GWT.create(AdminConstants.class);

        this.filterDisclosure.getHeaderTextAccessor().setText(constants.userTab_filterResults());

        this.deleteButton.setText(constants.userTab_deleteButton());
        this.deleteButton.addClickHandler(new DeleteClickHandler(this));

        this.lockButton.setText(constants.userTab_lockButton());
        this.lockButton.addClickHandler(new LockClickHandler(this));

        this.unlockButton.setText(constants.userTab_unlockButton());
        this.unlockButton.addClickHandler(new UnlockClickHandler(this));

        this.userNameLabel.setText(constants.userTab_userNameCriteria());
        this.userIdLabel.setText(constants.userTab_userIdCriteria());
        this.providerLabel.setText(constants.userTab_openIdProviderCriteria());
        this.adminLabel.setText(constants.userTab_adminCriteria());
        this.lockedLabel.setText(constants.userTab_lockedCriteria());
        this.registrationDateLabel.setText(constants.userTab_registrationDateCriteria());
        this.toLabel.setText(constants.userTab_to());

        this.refreshButton.setText(constants.userTab_refreshButton());
        this.refreshButton.addClickHandler(new RefreshClickHandler(this));

        this.clearButton.setText(constants.userTab_clearButton());
        this.clearButton.addClickHandler(new ClearClickHandler(this));
    }

    /** Setup the displayed table. */
    private void setupTable() {
        AdminConstants constants = GWT.create(AdminConstants.class);

        RegisteredUserKeyProvider keyProvider = new RegisteredUserKeyProvider();
        this.table = new SortableTable<RegisteredUser, RegisteredUserSortColumn>(constants.userTab_pageSize(), "100%", keyProvider);
        this.pager = table.getPager();

        this.table.addColumnSortHandler(new SortHandler(this));
        this.table.addSelectionColumn(5, Unit.PCT, new RegisteredUserKeyProvider());
        this.table.setNoRowsMessage(constants.userTab_noRows());

        addColumn(new UserNameColumn(), constants.userTab_userNameTitle(), 20);
        addColumn(new UserIdColumn(), constants.userTab_userIdTitle(), 15);
        addColumn(new RegistrationDateColumn(), constants.userTab_registrationDateTitle(), 10);
        addColumn(new OpenIdProviderColumn(), constants.userTab_openIdProviderTitle(), 10);
        addColumn(new LoginsColumn(), constants.userTab_loginsTitle(), 10);
        addColumn(new LastLoginColumn(), constants.userTab_lastLoginTitle(), 15);
        addColumn(new AdminColumn(), constants.userTab_adminTitle(), 7);
        addColumn(new LockedColumn(), constants.userTab_lockedTitle(), 8);
    }

    /** Add a column to the table. */
    private void addColumn(ColumnWithName<RegisteredUser, ?> column, String title, int width) {
        this.table.addColumn(column, title);
        this.table.setColumnWidth(column, width, Unit.PCT);
    }

    /** User id column. */
    protected static class UserIdColumn extends ColumnWithName<RegisteredUser, String> {
        public UserIdColumn() {
            super(USER_ID.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            return item == null || item.getUserId() == null ? "" : item.getUserId();
        }
    }

    /** User name column. */
    protected static class UserNameColumn extends ColumnWithName<RegisteredUser, String> {
        public UserNameColumn() {
            super(USER_NAME.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            return item == null || item.getUserName() == null ? "" : item.getUserName();
        }
    }

    /** Registration date column. */
    protected static class RegistrationDateColumn extends ColumnWithName<RegisteredUser, String> {
        public RegistrationDateColumn() {
            super(REGISTRATION_DATE.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            return item == null || item.getRegistrationDate() == null ? "" : GwtDateUtils.formatDate(item.getRegistrationDate());
        }
    }

    /** OpenId provider column. */
    protected static class OpenIdProviderColumn extends ColumnWithName<RegisteredUser, String> {
        public OpenIdProviderColumn() {
            super(OPEN_ID_PROVIDER.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            return item == null || item.getOpenIdProvider() == null ? "" : OpenIdProviderList.getProviderName(item.getOpenIdProvider());
        }
    }

    /** Logins column. */
    protected static class LoginsColumn extends ColumnWithName<RegisteredUser, String> {
        public LoginsColumn() {
            super(LOGINS.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            return item == null ? "" : String.valueOf(item.getLogins());
        }
    }

    /** Last login column. */
    protected static class LastLoginColumn extends ColumnWithName<RegisteredUser, String> {
        public LastLoginColumn() {
            super(LAST_LOGIN.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            return item == null || item.getLastLogin() == null ? "" : GwtDateUtils.formatTime(item.getLastLogin());
        }
    }

    /** Admin column. */
    protected static class AdminColumn extends ColumnWithName<RegisteredUser, String> {
        public AdminColumn() {
            super(ADMIN.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            AdminConstants constants = GWT.create(AdminConstants.class);
            return item == null ? "" : item.isAdmin() ? constants.userTab_yes() : constants.userTab_no();
        }
    }

    /** Locked column. */
    protected static class LockedColumn extends ColumnWithName<RegisteredUser, String> {
        public LockedColumn() {
            super(LOCKED.toString(), new TextCell());
            this.setSortable(true);
        }

        @Override
        public String getValue(RegisteredUser item) {
            AdminConstants constants = GWT.create(AdminConstants.class);
            return item == null ? "" : item.isLocked() ? constants.userTab_yes() : constants.userTab_no();
        }
    }

    /** Column sort handler. */
    protected static class SortHandler extends AbstractColumnSortEventHandler<UserTabView> {
        public SortHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getRefreshEventHandler();
        }
    }

    /** Delete click handler. */
    protected static class DeleteClickHandler extends AbstractEventHandler<UserTabView> implements ClickHandler {
        public DeleteClickHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public void onClick(ClickEvent event) {
            List<RegisteredUser> selected = this.getParent().getTable().getSelectedRecords();
            if (selected != null && !selected.isEmpty()) {
                this.getParent().getDeletePopup().showPopup();
                // later, the DeleteConfirmHandler will actually invoke the parent's delete event handler
            }
        }
    }

    /** Delete confirm handler. */
    protected static class DeleteConfirmHandler extends AbstractViewEventHandler<UserTabView> {
        public DeleteConfirmHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            if (this.getParent().getDeleteEventHandler() != null) {
                this.getParent().getDeleteEventHandler().handleEvent(event);
            }
        }
    }

    /** Lock click handler. */
    protected static class LockClickHandler extends AbstractClickHandler<UserTabView> {
        public LockClickHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getLockEventHandler();
        }
    }

    /** Unlock click handler. */
    protected static class UnlockClickHandler extends AbstractClickHandler<UserTabView> {
        public UnlockClickHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getUnlockEventHandler();
        }
    }

    /** Refresh click handler. */
    protected static class RefreshClickHandler extends AbstractClickHandler<UserTabView> {
        public RefreshClickHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getRefreshEventHandler();
        }
    }

    /** Clear click handler. */
    protected static class ClearClickHandler extends AbstractClickHandler<UserTabView> {
        public ClearClickHandler(UserTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getCriteriaResetEventHandler();
        }
    }

}
