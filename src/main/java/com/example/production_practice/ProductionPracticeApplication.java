package com.example.production_practice;

import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.service.RestaurantService;
import com.example.production_practice.service.ReviewService;
import com.example.production_practice.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/*
Необходимо создать следующие классы:
Сущности
●	Класс для посетителей ресторана с полями id (типа Long), имя, возраст, пол.
	Имя должно быть не обязательным (отзыв о ресторане можно оставить анонимно), всё остальное - обязательно.
●	Класс для ресторана с полями id (типа Long), название, описание (может быть пустым),
	тип кухни (сделать перечисление с типами “Европейская”, “Итальянская”, “Китайская”, и т.д.),
	средний чек на человека, оценка пользователей (типа BigDecimal).
●	Класс с оценками посетителей с полями id посетителя, id ресторана, оценка (int), текст отзыва (может быть пустым).

Слой работы с данными (репозитории).
В каждом классе хранить данные в приватном финальном поле типа List, все методы работают именно с этим полем.
●	Класс для работы с данными о посетителях, методы: save, remove, findAll.То есть класс, который сохраняет
	нового посетителя в наш список, удаляет оттуда его и находит все записи, остальные классы аналогично.
●	Класс для работы с данными о ресторанах, методы: save, remove, findAll
●	Класс для работы с данными об оценках, методы: save, remove, findAll, findById

Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
●	Класс для работы с данными о посетителях, методы: save, remove, findAll
●	Класс для работы с данными о ресторанах, методы: save, remove, findAll
●	Класс для работы с данными об оценках, методы: save, remove, findAll
	Не забудьте, что после добавления оценки и сохранения её необходимо пересчитать среднюю оценку ресторана.

Протестируйте реализованную логику. Для этого нужно добавить несколько посетителей, несколько ресторанов и оценки.
Все данные можно добавить путем использования конструкции @PostConstruct. Работоспособность сервисов можно
протестировать несколькими способами: получить объект контекста в main методе (SpringApplication.run() его возвращает)
и дальше поработать получив бины через context.getBean либо реализовать класс, наследующий CommandLineRunner,
переопределить метод run и в нем протестировать созданное приложение (это будет плюсом к вашей работе).

Доп. сложности:
●	Все поля классов, кроме слоя с сущностями, должны быть private final
●	Минимум boilerplate-кода за счет использования lombok
●	Разобрать все файлы по пакетам, чтобы было легко понять что за что отвечает.
	Поставить аннотации @Service и @Repository там где им место вместо аннотации @Component
*/

@SpringBootApplication
public class ProductionPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductionPracticeApplication.class, args);
	}

	@Component
	@RequiredArgsConstructor
	static class TestRunner implements CommandLineRunner {
		private final VisitorService visitorService;
		private final RestaurantService restaurantService;
		private final ReviewService reviewService;

		@Override
		public void run(String[] args) {
			System.out.println("Посетители:");
			for (Visitor visitor : visitorService.findAll()) System.out.println(visitor);

			System.out.println("\nРестораны:");
			for (Restaurant restaurant : restaurantService.findAll()) System.out.println(restaurant);

			System.out.println("\nОтзывы:");
			for (Review review : reviewService.findAll()) System.out.println(review);

			System.out.println("\nРейтинг ресторанов:");
			for (Restaurant restaurant : restaurantService.findAll()) {
				System.out.printf("У заведения '%s' рейтинг %s.\n", restaurant.getName(), restaurant.getRating());
			}

			Long visitorId = 2L;
			Long restaurantId = 1L;

			var review = reviewService.findById(visitorId, restaurantId);
			String visitorName = visitorService.findAll().stream()
					.filter(v -> v.getId().equals(visitorId))
					.map(v -> Optional.ofNullable(v.getName()).orElse("*аноним*"))
					.findFirst()
					.orElse("*посетитель не найден*");
			String restaurantName = restaurantService.findAll().stream()
					.filter(r -> r.getId().equals(restaurantId))
					.map(Restaurant::getName)
					.findFirst()
					.orElse("*ресторан не найден*");
			
			System.out.printf("\nОтзыв от %s о заведении %s:\n%s\n",
					visitorName,
					restaurantName,
                    review.map(value -> (value.getScore() + "/5 - " + value.getComment()))
							.orElse("отзыв не найден")
			);
		}
	}

}
