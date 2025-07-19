package com.example.production_practice;

import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.ReviewID;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import com.example.production_practice.repository.VisitorRepository;
import com.example.production_practice.service.RestaurantService;
import com.example.production_practice.service.ReviewService;
import com.example.production_practice.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import java.util.Optional;

@SpringBootApplication
public class ProductionPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductionPracticeApplication.class, args);
	}

	@Component
	@RequiredArgsConstructor
	static class TestRunner implements CommandLineRunner {
		private final VisitorRepository visitorRepository;
		private final RestaurantRepository restaurantRepository;
		private final ReviewRepository reviewRepository;

		@Override
		public void run(String[] args) {
//			System.out.println("Посетители:");
//			for (Visitor visitor : visitorRepository.findAll()) System.out.println(visitor);
//
//			System.out.println("\nРестораны:");
//			for (Restaurant restaurant : restaurantRepository.findAll()) System.out.println(restaurant);
//
//			System.out.println("\nОтзывы:");
//			for (Review review : reviewRepository.findAll()) System.out.println(review);
//
//			System.out.println("\nРейтинг ресторанов:");
//			for (Restaurant restaurant : restaurantRepository.findAll()) {
//				System.out.printf("У заведения '%s' рейтинг %s.\n", restaurant.getName(), restaurant.getRating());
//			}
//
//			Long visitorId = 2L;
//			Long restaurantId = 1L;
//
//			Review review = reviewRepository.findById(new ReviewID(visitorId, restaurantId)).orElseThrow();
//			String visitorName = visitorRepository.findAll().stream()
//					.filter(v -> v.getId().equals(visitorId))
//					.map(v -> Optional.ofNullable(v.getName()).orElse("*аноним*"))
//					.findFirst()
//					.orElse("*посетитель не найден*");
//			String restaurantName = restaurantRepository.findAll().stream()
//					.filter(r -> r.getId().equals(restaurantId))
//					.map(Restaurant::getName)
//					.findFirst()
//					.orElse("*ресторан не найден*");
//
//			System.out.printf("\nОтзыв от %s о заведении %s:\n%s\n",
//					visitorName,
//					restaurantName,
//                    review + "/5 - " + review.getComment()
//			);
		}
	}

}
