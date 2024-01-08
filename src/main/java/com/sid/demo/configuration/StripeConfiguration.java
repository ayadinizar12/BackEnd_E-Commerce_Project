package com.sid.demo.configuration;

import com.sid.demo.dtos.*;
import com.sid.demo.exceptions.*;
import com.sid.demo.models.*;
import com.sid.demo.repository.*;
import com.sid.demo.utils.*;
import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {

    @Autowired
    public StripeConfiguration(@Value("${stripe.secret-key}") String stripeSecretKey) {
        Stripe.apiKey = stripeSecretKey;
    }
}