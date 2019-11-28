package com.tms.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FlagExceptionMapper implements ExceptionMapper<FlagException> {

	@Override
	public Response toResponse(FlagException exception) {
			return Response.status(exception.getErrorCode()).entity(exception.getErrorMessage()).build();
	}

}
