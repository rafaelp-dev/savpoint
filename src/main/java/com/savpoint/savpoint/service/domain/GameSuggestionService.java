package com.savpoint.savpoint.service.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savpoint.savpoint.dtos.responses.GameSuggestionResponse;
import com.savpoint.savpoint.dtos.responses.GroqResponse;
import com.savpoint.savpoint.entities.UserProfileEntity;
import com.savpoint.savpoint.exceptions.InsufficientCreditsException;
import com.savpoint.savpoint.exceptions.NotFoundException;
import com.savpoint.savpoint.repositories.UserProfileRepository;
import com.savpoint.savpoint.service.external.GroqService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GameSuggestionService {

    private static final BigDecimal SUGGESTION_COST = new BigDecimal(100);

    private final GroqService groqService;
    private final ObjectMapper objectMapper;
    private final UserProfileRepository userProfileRepository;

    public GameSuggestionService(GroqService groqService, ObjectMapper objectMapper, UserProfileRepository userProfileRepository) {
        this.groqService = groqService;
        this.objectMapper = objectMapper;
        this.userProfileRepository = userProfileRepository;
    }

    public List<GameSuggestionResponse> generateSuggestions(Authentication authentication, String game, String experience) {
        String email = authentication.getName();

        UserProfileEntity profile = userProfileRepository.findByUser_Email(email)
                .orElseThrow(() -> new NotFoundException("Perfil do usuário não encontrado."));

        if (profile.getCredits().compareTo(SUGGESTION_COST) < 0) {
            throw new InsufficientCreditsException("Créditos insuficientes. São necessários 100 créditos para gerar sugestões.");
        }

        String prompt = buildPrompt(game, experience);
        GroqResponse groqResponse = groqService.sendPrompt(prompt);

        if (groqResponse == null || groqResponse.content() == null || groqResponse.content().isBlank()) {
            throw new RuntimeException("Não foi possível gerar sugestões de jogos.");
        }

        try {
            String json = extractJson(groqResponse.content());
            List<GameSuggestionResponse> suggestions = objectMapper.readValue(json, new TypeReference<List<GameSuggestionResponse>>() {});

            profile.setCredits(profile.getCredits().subtract(SUGGESTION_COST));
            userProfileRepository.save(profile);

            return suggestions;
        } catch (InsufficientCreditsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a resposta da IA: " + e.getMessage(), e);
        }
    }

    private String extractJson(String content) {
        String trimmed = content.trim();

        if (trimmed.startsWith("```json")) {
            trimmed = trimmed.substring(7);
        } else if (trimmed.startsWith("```")) {
            trimmed = trimmed.substring(3);
        }

        if (trimmed.endsWith("```")) {
            trimmed = trimmed.substring(0, trimmed.length() - 3);
        }

        return trimmed.trim();
    }

    private String buildPrompt(String game, String experience) {
        return """
                Você é um especialista em recomendação de jogos.
                
                Seu objetivo é recomendar jogos que proporcionem uma experiência semelhante àquela que o jogador deseja reviver, e não apenas jogos do mesmo gênero ou da mesma franquia.
                
                Considere principalmente a descrição do jogador e utilize o jogo informado apenas como contexto. Analise aspectos como mecânicas, exploração, combate, narrativa, atmosfera, progressão, liberdade, desafio e ritmo de gameplay.
                
                Entrada:
                
                Jogo escolhido:
                %s
                
                Experiência que o jogador deseja reviver:
                %s
                
                Regras:
                - Retorne exatamente 5 jogos.
                - Priorize a experiência desejada, não apenas gênero ou franquia.
                - Distribua o nível de semelhança da seguinte forma:
                  - 3 jogos com "MUITO"
                  - 1 jogo com "MÉDIO"
                  - 1 jogo com "POUCO"
                - O motivo deve ter apenas uma frase curta explicando por que o jogo foi recomendado.
                - Não retorne nenhuma explicação fora do JSON.
                
                Exemplo do formato da resposta:
                
                [
                  {
                    "name": "Nome completo do jogo",
                    "platforms": ["PC", "PlayStation 5", "Xbox Series X/S"],
                    "similarity": "MUITO",
                    "reason": "Combina pela liberdade de exploração e pelo foco em missões abertas."
                  },
                  {
                    "name": "Nome completo do jogo",
                    "platforms": ["PC", "PlayStation 5"],
                    "similarity": "MÉDIO",
                    "reason": "Compartilha alguns elementos de gameplay, mas com foco diferente."
                  },
                  {
                    "name": "Nome completo do jogo",
                    "platforms": ["PC", "Xbox Series X/S"],
                    "similarity": "POUCO",
                    "reason": "Apesar das diferenças, oferece uma experiência semelhante em um aspecto específico."
                  }
                ]
                """.formatted(game, experience);
    }
}
