package de.teamrocket.relaxo.rest.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Filter, welches prüft, ob der User ein Administrator ist.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAdmin {
}