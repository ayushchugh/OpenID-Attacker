/*
 * OpenID Attacker
 * (C) 2015 Christian Mainka & Christian Koßmann
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package wsattacker.sso.openid.attacker.attack.profile;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.beanutils.BeanUtils;
import org.jdesktop.observablecollections.ObservableCollections;
import wsattacker.sso.openid.attacker.attack.parameter.AttackParameter;
import wsattacker.sso.openid.attacker.attack.parameter.AttackParameterKeeper;

@XmlRootElement(name = "AttackProfileConfigurations")
@XmlAccessorType(XmlAccessType.NONE)
public class AttackProfileContainer implements Serializable {

    public List<AttackProfile> profileList = ObservableCollections.observableList(new ArrayList<AttackProfile>());

    public AttackProfileContainer() {
    }

    @XmlElement(name = "AttackProfile")
    public List<AttackProfile> getProfileList() {
        return profileList;
    }

    public void saveProfile(final String name, final String description, final AttackParameterKeeper configuration) {
        AttackProfile newProfile = new AttackProfile();
        newProfile.setName(name);
        newProfile.setDescription(description);
        newProfile.updateConfiguration(configuration);
        profileList.add(newProfile);
    }

    public void updateProfile(final int index, final String name, final String description) {
        AttackProfile toUpdate = profileList.get(index);
        toUpdate.setName(name);
        toUpdate.setDescription(description);
    }

    public void updateProfile(final int index, final String name, final String description, final AttackParameterKeeper configuration) {
        updateProfile(index, name, description);
        AttackProfile toUpdate = profileList.get(index);
        toUpdate.updateConfiguration(configuration);
    }

    public void deleteProfile(final int index) {
        profileList.remove(index);
    }

    public void loadProfile(AttackParameterKeeper toUpdate, final int index) {
        AttackProfile profile = profileList.get(index);
        AttackParameterKeeper configuration = profile.getConfiguration();
        for (AttackParameter readParameter : configuration) {
            AttackParameter originalParameter = toUpdate.getParameter(readParameter.getName());
            if (originalParameter != null) {
                try {
                    BeanUtils.copyProperties(originalParameter, readParameter);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new IllegalStateException("Could not load configuration", ex);
                }
            } else {
                toUpdate.addParameter(readParameter);
            }
        }
    }
}
