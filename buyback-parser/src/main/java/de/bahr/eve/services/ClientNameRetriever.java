package de.bahr.eve.services;

import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterInfoRequest;
import com.tlabs.eve.api.character.CharacterInfoResponse;
import com.tlabs.eve.net.DefaultEveNetwork;
import org.springframework.stereotype.Service;

/**
 * Created by michaelbahr on 24/01/2017.
 */
@Service
public class ClientNameRetriever {

    private EveNetwork eveNetwork = new DefaultEveNetwork();

    public String loadClientName(long issuerId) {
        CharacterInfoRequest request = new CharacterInfoRequest(String.valueOf(issuerId));
        CharacterInfoResponse response = eveNetwork.execute(request);
        CharacterInfo characterInfo = response.getCharacterInfo();
        if (characterInfo != null) {
            return characterInfo.getCharacterName();
        } else {
            return "n/a";
        }
    }
}
