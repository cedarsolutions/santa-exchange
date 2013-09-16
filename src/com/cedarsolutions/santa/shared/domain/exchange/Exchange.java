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
package com.cedarsolutions.santa.shared.domain.exchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Defines an exchange.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@XmlRootElement(name = "exchange")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "userId", "exchangeState", "name", "dateAndTime",
                                  "theme", "cost", "extraInfo", "organizer", "templateOverrides",
                                  "participants", "assignments" })
public class Exchange extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Unique identifier. */
    private Long id;  // because this is a Long, the id will be auto-generated

    /** User which owns this exchange. */
    private String userId;

    /** State of the exchange. */
    private ExchangeState exchangeState;

    /** Name of the exchange. */
    private String name;

    /** Free-form text describing the date and time of the exchange. */
    private String dateAndTime;

    /** Free-form text describing the theme of the exchange. */
    private String theme;

    /** Free-form text describing the cost of the exchange. */
    private String cost;

    /** Extra information to be placed into the generated email. */
    private String extraInfo;

    /** Organizer of the exchange. */
    private Organizer organizer;

    /** Template overrides to use, if any. */
    private TemplateConfig templateOverrides;

    /** Participants in the exchange. */
    private ParticipantSet participants;

    /** Assignments (if any). */
    private AssignmentSet assignments;

    /** Default constructor. */
    public Exchange() {
        this.organizer = new Organizer();
        this.templateOverrides = new TemplateConfig();
        this.participants = new ParticipantSet();
        this.assignments = null;  // yes, null -- it's optional
    }

    /** Copy constructor. */
    public Exchange(Exchange source) {
        this.refresh(source);
    }

    /** Refresh an exchange in-place, kind of like using the copy constructor. */
    public void refresh(Exchange source) {
        if (source == null) {
            this.organizer = new Organizer();
            this.templateOverrides = new TemplateConfig();
            this.participants = new ParticipantSet();
            this.assignments = null;  // yes, null -- it's optional
        } else {
            this.id = source.id;
            this.userId = source.userId;
            this.exchangeState = source.exchangeState;
            this.name = source.name;
            this.dateAndTime = source.dateAndTime;
            this.theme = source.theme;
            this.cost = source.cost;
            this.extraInfo = source.extraInfo;
            this.organizer = new Organizer(source.organizer);
            this.templateOverrides = new TemplateConfig(source.templateOverrides);
            this.participants = new ParticipantSet(source.participants);
            this.assignments = source.assignments == null ? null : new AssignmentSet(source.assignments);
        }
    }

    /** Get next participant id. */
    public long getNextParticipantId() {
        long largestId = 0;

        if (this.getParticipants() != null) {
            for (Participant participant : this.getParticipants()) {
                if (participant.getId() > largestId) {
                    largestId = participant.getId();
                }
            }
        }

        return largestId + 1;
    }

    /**
     * Replace a participant in the participants list.
     * @param participant  Participant to replace, by id
     * @return True if the participant was found, false otherwise.
     */
    public boolean replaceParticipant(Participant participant) {
        boolean found = false;

        if (participant != null && participant.getId() != null) {
            if (this.getParticipants() != null) {
                for (int index = 0; index < this.getParticipants().size(); index++) {
                    if (equals(this.getParticipants().get(index).getId(), participant.getId())) {
                        this.getParticipants().set(index, participant);
                        found = true;
                        break;
                    }
                }
            }
        }

        return found;
    }

    /**
     * Replace a participant from the participants list.
     * @param participant  Participant to remove, by id
     * @return True if the participant was found, false otherwise.
     */
    public boolean removeParticipant(Participant participant) {
        boolean found = false;

        if (participant != null && participant.getId() != null) {
            if (this.getParticipants() != null) {
                for (int index = 0; index < this.getParticipants().size(); index++) {
                    if (equals(this.getParticipants().get(index).getId(), participant.getId())) {
                        this.getParticipants().remove(index);
                        found = true;
                        break;
                    }
                }
            }
        }

        return found;
    }

    /**
     * Get a participant from the participants list by id.
     * @param participantId   Id of the participant to retrieve
     * @return Participant with the passed-in id, or null if that participant is not found.
     */
    public Participant getParticipantById(long participantId) {
        if (this.getParticipants() != null) {
            for (int index = 0; index < this.getParticipants().size(); index++) {
                if (equals(this.getParticipants().get(index).getId(), participantId)) {
                    return this.getParticipants().get(index);
                }
            }
        }

        return null;
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        Exchange other = (Exchange) obj;
        return new EqualsBuilder()
                    .append(this.id, other.id)
                    .append(this.userId, other.userId)
                    .append(this.exchangeState, other.exchangeState)
                    .append(this.name, other.name)
                    .append(this.dateAndTime, other.dateAndTime)
                    .append(this.theme, other.theme)
                    .append(this.cost, other.cost)
                    .append(this.extraInfo, other.extraInfo)
                    .append(this.organizer, other.organizer)
                    .append(this.templateOverrides, other.templateOverrides)
                    .append(this.participants, other.participants)
                    .append(this.assignments, other.assignments)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.id)
                    .append(this.userId)
                    .append(this.exchangeState)
                    .append(this.name)
                    .append(this.dateAndTime)
                    .append(this.theme)
                    .append(this.cost)
                    .append(this.extraInfo)
                    .append(this.organizer)
                    .append(this.templateOverrides)
                    .append(this.participants)
                    .append(this.assignments)
                    .toHashCode();
    }

    /**
     * Indicates whether two integer values are equal (null-safe).
     * @param obj1  First object to compare
     * @param obj2  Second object to compare
     * @return True if the objects are equal, false otherwise.
     */
    private static boolean equals(Long obj1, Long obj2) {
        if (obj1 == null) {
            return obj2 == null;
        } else if (obj2 == null) {
            return obj1 == null;
        } else {
            return obj1.equals(obj2);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ExchangeState getExchangeState() {
        return this.exchangeState;
    }

    public void setExchangeState(ExchangeState state) {
        this.exchangeState = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public TemplateConfig getTemplateOverrides() {
        return templateOverrides;
    }

    public void setTemplateOverrides(TemplateConfig templateOverrides) {
        this.templateOverrides = templateOverrides;
    }

    public ParticipantSet getParticipants() {
        return participants;
    }

    public void setParticipants(ParticipantSet participants) {
        this.participants = participants;
    }

    public AssignmentSet getAssignments() {
        return this.assignments;
    }

    public void setAssignments(AssignmentSet assignments) {
        this.assignments = assignments;
    }
}
