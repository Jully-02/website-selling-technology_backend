package vn.jully.website_selling_technology_backend.services.role;

import vn.jully.website_selling_technology_backend.dtos.RoleDTO;
import vn.jully.website_selling_technology_backend.entities.Role;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface IRoleService {
    Role insertRole (RoleDTO roleDTO);

    Role updateRole (Long id, RoleDTO roleDTO) throws DataNotFoundException;

    Role getRole (Long id) throws DataNotFoundException;

    List<Role> getAllRoles ();

    void deleteRole (Long id);
}
