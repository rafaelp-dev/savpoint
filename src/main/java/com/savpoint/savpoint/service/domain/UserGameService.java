package com.savpoint.savpoint.service.domain;

import com.savpoint.savpoint.dtos.requests.UserGameRequest;
import com.savpoint.savpoint.dtos.responses.MessageResponse;
import com.savpoint.savpoint.entities.GameEntity;
import com.savpoint.savpoint.entities.UserGameEntity;
import com.savpoint.savpoint.entities.UserProfileEntity;
import com.savpoint.savpoint.exceptions.ConflictException;
import com.savpoint.savpoint.exceptions.NotFoundException;
import com.savpoint.savpoint.repositories.GameRepository;
import com.savpoint.savpoint.repositories.UserGameRepository;
import com.savpoint.savpoint.repositories.UserProfileRepository;
import com.savpoint.savpoint.dtos.responses.UserGameResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGameService {

        private final UserGameRepository userGameRepository;
        private final UserProfileRepository userProfileRepository;
        private final GameRepository gameRepository;

        public UserGameService(UserGameRepository userGameRepository, UserProfileRepository userProfileRepository,
                        GameRepository gameRepository) {
                this.userGameRepository = userGameRepository;
                this.userProfileRepository = userProfileRepository;
                this.gameRepository = gameRepository;
        }

        public MessageResponse addGameToProfile(Authentication authentication, UserGameRequest request) {
                String email = authentication.getName();

                UserProfileEntity userProfile = userProfileRepository.findByUser_Email(email)
                                .orElseThrow(() -> new NotFoundException(
                                                "Perfil não encontrado para o usuário atual. Crie um perfil primeiro."));

                GameEntity game = gameRepository.findById(request.gameId())
                                .orElseThrow(() -> new NotFoundException(
                                                "Jogo não encontrado no banco de dados local."));

                if (userGameRepository
                                .findByProfile_UserProfileIdAndGame_GameId(userProfile.getUserProfileId(),
                                                game.getGameId())
                                .isPresent()) {
                        throw new ConflictException("Este jogo já foi adicionado ao seu perfil.");
                }

                UserGameEntity userGame = new UserGameEntity(
                                request.review(),
                                userProfile,
                                game,
                                request.status(),
                                request.favorite(),
                                request.rating());

                userGameRepository.save(userGame);

                return new MessageResponse("Jogo adicionado ao perfil com sucesso!");
        }

        public List<UserGameResponse> listUserGames(Authentication authentication) {
                String email = authentication.getName();

                UserProfileEntity userProfile = userProfileRepository.findByUser_Email(email)
                                .orElseThrow(() -> new NotFoundException(
                                                "Perfil não encontrado para o usuário atual. Crie um perfil primeiro."));

                List<UserGameEntity> userGames = userGameRepository
                                .findByProfile_UserProfileId(userProfile.getUserProfileId());

        return userGames.stream().map(ug -> new UserGameResponse(
                                ug.getUserGameId(),
                                ug.getGame().getGameId(),
                                ug.getGame().getTitle(),
                                ug.getGame().getCoverUrl(),
                                ug.getStatus(),
                                ug.getFavorite(),
                                ug.getRating(),
                                ug.getReview(),
                                ug.getAddedAt())).collect(Collectors.toList());
        }

        public List<UserGameResponse> listFavoriteUserGames(Authentication authentication) {
                String email = authentication.getName();

                UserProfileEntity userProfile = userProfileRepository.findByUser_Email(email)
                                .orElseThrow(() -> new NotFoundException(
                                                "Perfil não encontrado para o usuário atual. Crie um perfil primeiro."));

                List<UserGameEntity> userGames = userGameRepository
                                .findByProfile_UserProfileIdAndFavoriteTrue(userProfile.getUserProfileId());

                return userGames.stream().map(ug -> new UserGameResponse(
                                ug.getUserGameId(),
                                ug.getGame().getGameId(),
                                ug.getGame().getTitle(),
                                ug.getGame().getCoverUrl(),
                                ug.getStatus(),
                                ug.getFavorite(),
                                ug.getRating(),
                                ug.getReview(),
                                ug.getAddedAt())).collect(Collectors.toList());
        }
}
