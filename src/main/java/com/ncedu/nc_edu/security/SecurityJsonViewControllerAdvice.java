package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.dto.resources.OwnableResource;
import com.ncedu.nc_edu.dto.resources.RecipeResource;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.models.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    private SecurityAccessResolver securityAccessResolver;

    public SecurityJsonViewControllerAdvice(@Autowired SecurityAccessResolver securityAccessResolver) {
        this.securityAccessResolver = securityAccessResolver;
    }

    @Override
    protected void beforeBodyWriteInternal(
            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (bodyContainer.getValue() instanceof RepresentationModel
                && bodyContainer.getSerializationView() == null
                && securityAccessResolver.getUser() != null) {

            bodyContainer.setSerializationView(View.getView(securityAccessResolver.getHeadAuthority()));
            if (bodyContainer.getValue() instanceof CollectionModel) {
                //Collection model = ((CollectionModel) bodyContainer.getValue()).getContent();
                //if (model.isEmpty()) {
                    return;
                //}
            }
            if (Arrays.asList(bodyContainer.getValue().getClass().getInterfaces()).contains(OwnableResource.class)
                    && ((OwnableResource)bodyContainer.getValue()).getOwnerId().equals(securityAccessResolver.getUser().getId())) {
                bodyContainer.setSerializationView(View.Owner.class);
            }
        }
    }
}
