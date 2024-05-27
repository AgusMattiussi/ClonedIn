package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.ContactStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.exceptions.ContactNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.ContactDTO;
import ar.edu.itba.paw.webapp.form.ContactForm;
import ar.edu.itba.paw.webapp.form.UpdateContactStatusForm;
import ar.edu.itba.paw.webapp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;

@Path("api/contacts")
@Component
public class ContactController {

    private static final int CONTACTS_PER_PAGE = 10;
    private static final String S_CONTACTS_PER_PAGE = "10";
    private static final String JOB_OFFER_OWNER = "@securityValidator.isJobOfferOwner(#userId, #jobOfferId)";
    private static final String USER_OR_JOB_OFFER_OWNER = "hasAuthority('USER') or @securityValidator.isJobOfferOwner(#jobOfferId)";


    @Context
    private UriInfo uriInfo;

    @Autowired
    private ContactService contactService;


    /** Get all contacts **/
    @GET
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize("@securityValidator.isGetContactsValid(#userId, #enterpriseId, #jobOfferId)")
    public Response getContacts(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                    @QueryParam("pageSize") @DefaultValue(S_CONTACTS_PER_PAGE)
                                        @Min(1) @Max(2*CONTACTS_PER_PAGE) final int pageSize,
                                    @QueryParam("status") final ContactStatus status,
                                    @QueryParam("filledBy") @DefaultValue("any") FilledBy filledBy,
                                    @QueryParam("sortBy") @DefaultValue("any") final ContactSorting sortBy,
                                    @QueryParam("jobOfferId") final Long jobOfferId,
                                    @QueryParam("enterpriseId") final Long enterpriseId,
                                    @QueryParam("userId") final Long userId) {

        PaginatedResource<Contact> contacts = contactService.getContactsForRole(SecurityUtils.getPrincipalRole(),
                SecurityUtils.getPrincipalId(), enterpriseId, jobOfferId, userId, filledBy,
                status, sortBy, page, pageSize);


        if(contacts.isEmpty())
            return Response.noContent().build();

        List<ContactDTO> contactDTOs = contacts.getPage().stream()
                .map(c -> ContactDTO.fromContact(uriInfo, c, SecurityUtils.getPrincipalRole() == Role.ENTERPRISE))
                .collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactDTOs) {}), page,
                contacts.getMaxPages());
    }

    /** Create new contact **/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('USER') or @securityValidator.isJobOfferOwner(#contactForm.jobOfferId)")
    public Response contactUser(@Valid @NotNull final ContactForm contactForm) {


        Contact contact = contactService.addContact(SecurityUtils.getPrincipalRole(), SecurityUtils.getPrincipalId(),
                contactForm.getJobOfferId(), contactForm.getUserId(), contactForm.getMessage());

        // TODO: Definir path
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(contact.getContactId())
                .build();
        return Response.created(uri).build();
    }

    /** Get Contact by ID **/
    @GET
    @Path("/{contactId:\\d+-\\d+}") // userId-jobOfferId
    @Produces(ClonedInMediaType.CONTACT_V1)
    @PreAuthorize("@securityValidator.canAccessContact(#contactId)")
    public Response getContact(@PathParam("contactId") final String contactId) {
        ContactDTO contactDTO = contactService.getContact(contactId)
                .map(c -> ContactDTO.fromContact(uriInfo, c, SecurityUtils.getPrincipalRole() == Role.ENTERPRISE))
                .orElseThrow(() -> new ContactNotFoundException(contactId));

        return Response.ok(contactDTO).build();
    }

    /** Update contact status **/
    @POST
    @Path("/{contactId:\\d+-\\d+}")
    @PreAuthorize("@securityValidator.canAccessContact(#contactId)")
    public Response updateContactStatus(@PathParam("contactId") final String contactId,
                                       @NotNull final UpdateContactStatusForm statusForm) {
        contactService.updateContactStatus(SecurityUtils.getPrincipalRole(), contactId, statusForm.getStatusEnum());

        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.seeOther(uri).build();
    }


}
