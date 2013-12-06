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
import javax.xml.bind.annotation.XmlType;

import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Participant in an exchange.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "name", "nickname", "emailAddress", "templateOverrides", "conflicts" })
public class Participant extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Unique identifier. */
    private Long id;

    /** Participant's full name. */
    private String name;

    /** Participant's nickname. */
    private String nickname;

    /** Participant's email address. */
    private String emailAddress;

    /** Template overrides to use, if any. */
    private TemplateConfig templateOverrides;

    /**
     * Set of conflicts for this participant.
     * A conflict is someone that this participant is not allowed to give a gift to.
     */
    private ParticipantSet conflicts;

    /** Default constructor. */
    public Participant() {
        this(null, null, null, null);
    }

    /** Values constructor. */
    public Participant(Long id) {
        this(id, null, null, null);
    }

    /** Values constructor. */
    public Participant(Long id, String name, String nickname, String emailAddress) {
        this(id, name, nickname, emailAddress, null);
    }

    /** Values constructor. */
    public Participant(Long id, String name, String nickname, String emailAddress, ParticipantSet conflicts) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.emailAddress = emailAddress;
        this.templateOverrides = new TemplateConfig();
        this.conflicts = new ParticipantSet(conflicts);
    }

    /** Copy constructor. */
    public Participant(Participant source) {
        if (source == null) {
            this.templateOverrides = new TemplateConfig();
            this.conflicts = new ParticipantSet();
        } else {
            this.id = source.id;
            this.name = source.name;
            this.nickname = source.nickname;
            this.emailAddress = source.emailAddress;
            this.templateOverrides = new TemplateConfig(source.templateOverrides);
            this.conflicts = new ParticipantSet(source.conflicts);
        }
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        Participant other = (Participant) obj;
        return new EqualsBuilder()
                    .append(this.id, other.id)
                    .append(this.name, other.name)
                    .append(this.nickname, other.nickname)
                    .append(this.emailAddress, other.emailAddress)
                    .append(this.templateOverrides, other.templateOverrides)
                    .append(this.conflicts, other.conflicts)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.id)
                    .append(this.name)
                    .append(this.nickname)
                    .append(this.emailAddress)
                    .append(this.conflicts)
                    .append(this.templateOverrides)
                    .toHashCode();
    }

    /** Add a conflict, keyed on participant id. */
    public void addConflict(Participant conflict) {
        for (Participant participant : this.conflicts) {
            if (conflict.getId().equals(participant.getId())) {
                return;  // same id is already there, so ignore it
            }
        }

        this.conflicts.add(conflict);
    }

    /** Remove a conflict, keyed on participant id. */
    public void removeConflict(Participant conflict) {
        for (int i = 0; i < this.conflicts.size(); i++) {
            Participant participant = this.conflicts.get(i);
            if (conflict.getId().equals(participant.getId())) {
                this.conflicts.remove(i);
                return;
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public TemplateConfig getTemplateOverrides() {
        return templateOverrides;
    }

    public void setTemplateOverrides(TemplateConfig templateOverrides) {
        this.templateOverrides = templateOverrides;
    }

    public ParticipantSet getConflicts() {
        return conflicts;
    }

    public void setConflicts(ParticipantSet conflicts) {
        this.conflicts = conflicts;
    }

}
