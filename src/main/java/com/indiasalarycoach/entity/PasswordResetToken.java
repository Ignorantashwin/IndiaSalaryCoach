package com.indiasalarycoach.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

  private  Long id;

   private String token;

   private LocalDateTime expiresAt;

   private boolean used;

   private LocalDateTime createdAt;

   @ManyToOne
   @JoinColumn(name = "user_id")
   private User user;

    // public void setUser(User user2) {
    //   this.setUser(user2);
    // }

    // public void setToken(String token2) {
    //     this.setToken(token2);
    // }

    // public void setUsed(boolean b) {
    // this.setUsed(b);
    // }

    // public void setExpiresAt(LocalDateTime plusMinutes) {
    //     this.setExpiresAt(plusMinutes);
    // }


}
