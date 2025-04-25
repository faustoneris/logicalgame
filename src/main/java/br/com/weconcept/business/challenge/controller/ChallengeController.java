package br.com.weconcept.business.challenge.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.weconcept.business.challenge.models.ChallengeResult;
import br.com.weconcept.business.challenge.models.ChallengeType;
import br.com.weconcept.business.challenge.services.ChallengeService;


@RestController
@RequestMapping("challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("fibonacci")
    public ResponseEntity<Integer> fibonacci(@RequestParam int number) {
        return ResponseEntity.ok(challengeService.executeFibonacci(number));
    }

    @PostMapping("palindrome")
    public ResponseEntity<Boolean> palindrome(@RequestParam String word) {
        return ResponseEntity.ok(challengeService.isPalindrome(word));
    }

    @PostMapping("sort")
    public ResponseEntity<int[]> sort(@RequestBody int[] anyNumbers) {
        return ResponseEntity.ok(challengeService.customSort(anyNumbers));
    }

    @PostMapping("score")
    public ResponseEntity<ChallengeResult> score(
            @RequestParam String playerId,
            @RequestParam String tournamentId,
            @RequestParam ChallengeType type
    ) {
        return ResponseEntity.ok(challengeService.registerScore(playerId, tournamentId, type));
    }
}
