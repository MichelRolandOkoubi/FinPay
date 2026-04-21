package com.finpay.common.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false)
    private Long amount; // in cents

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String method; // CARD | SEPA | SWIFT | APPLE_PAY | GOOGLE_PAY

    @Column(nullable = false)
    private String status; // PENDING | SUCCEEDED | FAILED | REFUNDED

    private String stripePaymentIntentId;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
