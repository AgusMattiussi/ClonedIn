package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.ContactDTO;
import ar.edu.itba.paw.webapp.security.SecurityValidator;
import ar.edu.itba.paw.webapp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;

@Path("api/contacts")
@Component
public class ContactController {

    private static final int CONTACTS_PER_PAGE = 10;
    private static final String S_CONTACTS_PER_PAGE = "10";

    @Context
    private UriInfo uriInfo;

    @Autowired
    private ContactService contactService;

    /* TODO:
    * Una empresa no deberia poder usar el parametro "enterpriseID" a menos que coincida con su ID ni un jobOfferId
    * que no le pertenezca
    * Un usuario no deberia poder usar el parametro "userID" a menos que coincida con su ID.
    * */

    @GET
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize("@securityValidator.isGetContactsValid(#userId, #enterpriseId, #jobOfferId)")
    public Response getContacts(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                    @QueryParam("pageSize") @DefaultValue(S_CONTACTS_PER_PAGE)
                                        @Min(1) @Max(2*CONTACTS_PER_PAGE) final int pageSize,
                                    @QueryParam("status") final JobOfferStatus status,
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
}
