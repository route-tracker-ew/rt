package com.kpi.routetracker.telegram.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kpi.routetracker.dto.TelegramParcelDto;
import com.kpi.routetracker.model.parcel.Parcel;
import com.kpi.routetracker.model.route.Route;
import com.kpi.routetracker.model.user.Account;
import com.kpi.routetracker.repo.ParcelRepository;
import com.kpi.routetracker.services.account.AccountService;
import com.kpi.routetracker.services.account.TelegramAccountService;
import com.kpi.routetracker.services.route.RouteService;
import com.kpi.routetracker.telegram.MessageUtils;
import com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationDate;
import com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState;
import com.kpi.routetracker.utils.mapper.TelegramParcelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.kpi.routetracker.telegram.MessageUtils.sendMessage;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_APARTMENT_RECEIVER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_APARTMENT_SENDER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_CITY_RECEIVER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_CITY_SENDER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_COUNTRY_RECEIVER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_COUNTRY_SENDER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_HOUSE_RECEIVER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_HOUSE_SENDER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_PARCEL_COUNT;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_RECEIVER_LAST_NAME;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_RECEIVER_NAME;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_RECEIVER_PHONE;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_STREET_RECEIVER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.ASK_STREET_SENDER;
import static com.kpi.routetracker.telegram.model.parcel.TelegramParcelCreationState.START;

@Slf4j
@Service
@AllArgsConstructor
public class TelegramParcelService {

    final Map<Long, TelegramParcelCreationState> creatingParcelStateMap = new HashMap<>();
    final Map<Long, TelegramParcelCreationDate> creatingParcelDataMap = new HashMap<>();
    final Set<Long> deleteParcelStates = new HashSet<>();
    final Map<Long, Long> selectRouteMap = new HashMap<>();

    final TelegramAccountService telegramAccountService;
    final RouteService routeService;
    final AccountService accountService;
    final ParcelRepository parcelRepository;
    final TelegramAccountManagementService telegramAccountManagementService;

    public void startParcelCreating(long chatId, TelegramLongPollingBot bot) {
        if (telegramAccountService.getByChatId(chatId).isPresent()) {
            // Створюємо повідомлення з інлайн-клавіатурою
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Оберіть маршрут:");

            // Створюємо інлайн клавіатуру
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            // Створюємо список рядів клавіатури
            List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

            for (Route route : routeService.getAll()) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(
                        route.getSourceCountry() + "(" + route.getSourceCity() + ")" + " - " + route.getDestinationCountry() + "(" + route.getDestinationCity() + ")");
                inlineKeyboardButton.setCallbackData(String.valueOf(route.getId()));
                row.add(inlineKeyboardButton);
                keyboardRows.add(row);
            }
            // Встановлюємо клавіатуру в повідомлення
            inlineKeyboardMarkup.setKeyboard(keyboardRows);
            message.setReplyMarkup(inlineKeyboardMarkup);
            creatingParcelStateMap.put(chatId, START);
            // Відправляємо повідомлення
            try {
                bot.execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sendMessage(chatId, "Спочатку потрібно зареєструватися", bot);
        }

    }

    public void handleParcelCreation(long chatId, String messageText, TelegramLongPollingBot bot) {
        TelegramParcelCreationState currentState = creatingParcelStateMap.get(chatId);
        TelegramParcelCreationDate parcelCreationDate = creatingParcelDataMap.get(chatId);

        switch (currentState) {
            case START:
                selectRouteMap.put(chatId, Long.valueOf(messageText));
                creatingParcelStateMap.put(chatId, ASK_COUNTRY_SENDER);
                creatingParcelDataMap.put(chatId, new TelegramParcelCreationDate());
                sendMessage(chatId, "🌍 Введіть країну відправника:", bot);
                break;
            case ASK_COUNTRY_SENDER:
                parcelCreationDate.setCountrySender(messageText);
                creatingParcelStateMap.put(chatId, ASK_CITY_SENDER);
                sendMessage(chatId, "🏙️ Введіть місто відправника:", bot);
                break;
            case ASK_CITY_SENDER:
                parcelCreationDate.setCitySender(messageText);
                creatingParcelStateMap.put(chatId, ASK_STREET_SENDER);
                sendMessage(chatId, "🚏 Введіть вулицю відправника:", bot);
                break;
            case ASK_STREET_SENDER:
                if (!messageText.equals("_")) {
                    parcelCreationDate.setStreetSender(messageText);
                }
                creatingParcelStateMap.put(chatId, ASK_HOUSE_SENDER);
                sendMessage(chatId, "🏠 Введіть номер будинку відправника (можна пропустити):", bot);
                break;
            case ASK_HOUSE_SENDER:
                if (!messageText.equals("_")) {
                    parcelCreationDate.setHouseNumberSender(messageText);
                }
                creatingParcelStateMap.put(chatId, ASK_APARTMENT_SENDER);
                sendMessage(chatId, "🏢 Введіть номер квартири відправника (можна пропустити):", bot);
                break;
            case ASK_APARTMENT_SENDER:
                if (!messageText.equals("_")) {
                    parcelCreationDate.setApartmentNumberSender(Integer.valueOf(messageText));
                }
                creatingParcelStateMap.put(chatId, ASK_RECEIVER_PHONE);
                sendMessage(chatId, "📞 Введіть номер телефону отримувача:", bot);
                break;
            case ASK_RECEIVER_PHONE:
                parcelCreationDate.setReceiverPhone(messageText);
                creatingParcelStateMap.put(chatId, ASK_RECEIVER_NAME);
                sendMessage(chatId, "👤 Введіть ім'я отримувача:", bot);
                break;
            case ASK_RECEIVER_NAME:
                parcelCreationDate.setReceiverName(messageText);
                creatingParcelStateMap.put(chatId, ASK_RECEIVER_LAST_NAME);
                sendMessage(chatId, "👤 Введіть прізвище отримувача:", bot);
                break;
            case ASK_RECEIVER_LAST_NAME:
                parcelCreationDate.setReceiverLastName(messageText);
                creatingParcelStateMap.put(chatId, ASK_COUNTRY_RECEIVER);
                sendMessage(chatId, "🌍 Введіть країну отримувача:", bot);
                break;
            case ASK_COUNTRY_RECEIVER:
                parcelCreationDate.setCountryReceiver(messageText);
                creatingParcelStateMap.put(chatId, ASK_CITY_RECEIVER);
                sendMessage(chatId, "🏙️ Введіть місто отримувача:", bot);
                break;
            case ASK_CITY_RECEIVER:
                parcelCreationDate.setCityReceiver(messageText);
                creatingParcelStateMap.put(chatId, ASK_STREET_RECEIVER);
                sendMessage(chatId, "🏢 Введіть вулицю отримувача (можна пропустити):", bot);
                break;
            case ASK_STREET_RECEIVER:
                if (!messageText.equals("_")) {
                    parcelCreationDate.setStreetReceiver(messageText);
                }
                creatingParcelStateMap.put(chatId, ASK_HOUSE_RECEIVER);
                sendMessage(chatId, "🏡 Введіть номер будинку отримувача (можна пропустити):", bot);
                break;
            case ASK_HOUSE_RECEIVER:
                if (!messageText.equals("_")) {
                    parcelCreationDate.setHouseReceiver(messageText);
                }
                creatingParcelStateMap.put(chatId, ASK_APARTMENT_RECEIVER);
                sendMessage(chatId, "🏠 Введіть номер квартири отримувача (можна пропустити):", bot);
                break;
            case ASK_APARTMENT_RECEIVER:
                if (!messageText.equals("_")) {
                    parcelCreationDate.setApartmentReceiver(Integer.valueOf(messageText));
                }
                creatingParcelStateMap.put(chatId, ASK_PARCEL_COUNT);
                sendMessage(chatId, "📦 Введіть кількість місць у посилці:", bot);
                break;
            case ASK_PARCEL_COUNT:
                parcelCreationDate.setParcelCount(Integer.parseInt(messageText));
                completeParcelCreating(chatId, parcelCreationDate, bot);
                sendMessage(chatId, "✅ Посилка успішно створена!", bot);
                break;
        }
    }

    private void completeParcelCreating(long chatId, TelegramParcelCreationDate parcelCreationDate, TelegramLongPollingBot bot) {
        //Save the user to the database or perform other registration logic
        var sender = telegramAccountService.getByChatId(chatId).get().getAccount();
        var receiver = createAccount(parcelCreationDate.getReceiverPhone(), parcelCreationDate.getReceiverName(), parcelCreationDate.getReceiverLastName());
        var route = routeService.getById(selectRouteMap.get(chatId));

        var result = createParcel(parcelCreationDate, sender, receiver, route);
        try {
            sendMessage(chatId, prepareFindParcelResponseText(TelegramParcelMapper.mapper.toDto(result)).toString(), bot);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Clear the registration state
        creatingParcelStateMap.remove(chatId);
        creatingParcelDataMap.remove(chatId);
        selectRouteMap.remove(chatId);
    }

    public void startDeletingParcel(long chatId, TelegramLongPollingBot bot) {
        deleteParcelStates.add(chatId);
        sendMessage(chatId, "Введіть номер посилки:", bot);
    }

    public void continueDeletingParcel(long chatId, String id, TelegramLongPollingBot bot) {
        var telegramAccount = telegramAccountService.getByChatId(chatId);
        if (telegramAccount.isPresent()) {
            deleteParcelStates.remove(chatId);
            var parcel = parcelRepository.findById(Long.valueOf(id));
            if (parcel.isPresent() && parcel.get().getSender().equals(telegramAccount.get().getAccount())) {
                parcelRepository.deleteById(Long.valueOf(id));
                sendMessage(chatId, "Посилка успішно видаленна", bot);
            } else {
                sendMessage(chatId, "Ви не можете видали цю посилку", bot);
            }
        } else {
            sendMessage(chatId, "Спочатку потрібно зареєструватися", bot);
        }

    }

    public Account createAccount(String phoneNumber, String firstName, String lastName) {
        var account = accountService.getAccountByPhoneNumber(phoneNumber);
        return account.orElseGet(() -> accountService.createWithoutPassword(phoneNumber, firstName, lastName));
    }

    public Parcel createParcel(TelegramParcelCreationDate parcelCreationDate, Account sender, Account receiver, Route route) {
        return parcelRepository.save(parcelCreationDate.toParcel(sender, receiver, route));
    }

    public boolean isParcelDeletingStarted(Long chatId) {
        return deleteParcelStates.contains(chatId);
    }

    public boolean isParcelCreationStarted(Long chatId) {
        return creatingParcelStateMap.containsKey(chatId);
    }

    public void findParcels(long chatId, String phoneNumber, TelegramLongPollingBot bot) throws JsonProcessingException {
        List<TelegramParcelDto> parcels = getParcelsByPhoneNumber(phoneNumber);

        if (parcels.isEmpty()) {
            sendMessage(chatId, "Посилка не знайдена за номером телефону: " + phoneNumber, bot);
        } else {
            for (TelegramParcelDto parcel : parcels) {
                MessageUtils.sendLocation(chatId, 49.213255576831784, 28.44080015833143, bot);
                sendMessage(chatId, prepareFindParcelResponseText(parcel).toString(), bot);
            }
        }
    }

    public void findAllParcels(long chatId, TelegramLongPollingBot bot) throws JsonProcessingException {
        var telegramAccount = telegramAccountManagementService.getTelegramAccount(chatId);
        if (telegramAccount != null) {
            findParcels(chatId, telegramAccount.getAccount().getPhoneNumber(), bot);
        } else {
            sendMessage(chatId, "Вас не знайдено у системі. Зареєструйтеся за номером телефону, якщо Ви цього ще не зробили", bot);
        }

    }

    private List<TelegramParcelDto> getParcelsByPhoneNumber(String phoneNumber) {
        return TelegramParcelMapper.mapper.toDto(parcelRepository.findAllBySender_PhoneNumber(phoneNumber));
    }

    public StringBuilder prepareFindParcelResponseText(TelegramParcelDto telegramParcelDto) {
        StringBuilder builder = new StringBuilder();

        builder.append("Посилка №: ")
                .append(telegramParcelDto.getId())
                .append("\n");

        builder.append("\n");
        if (telegramParcelDto.getSender() != null) {
            var sender = telegramParcelDto.getSender();
            builder.append("Відправник 📦\n").append(" • 📞 Номер телефону: ").append(sender.getPhoneNumber()).append("\n").append(" • 👤 Ім’я: ")
                    .append(sender.getFirstName())
                    .append("\n").append(" • 👤 Прізвище: ").append(sender.getLastName()).append("\n");
        }

        builder.append("\n");

        if (telegramParcelDto.getSourceCountry() != null || telegramParcelDto.getSourceCity() != null) {
            builder.append("Адреса відправки 🏠\n");
            if (telegramParcelDto.getSourceCountry() != null) {
                builder.append(" • 🌍 Країна відправки: ").append(telegramParcelDto.getSourceCountry()).append("\n");
            }
            if (telegramParcelDto.getSourceCity() != null) {
                builder.append(" • 🏙️ Місто відправки: ").append(telegramParcelDto.getSourceCity()).append("\n");
            }
            builder.append("\n");
        }

        var receiver = telegramParcelDto.getReceiver();
        builder.append("Отримувач 🎁\n").append(" • 📞 Номер телефону: ").append(receiver.getPhoneNumber()).append("\n").append(" • 👤 Ім’я: ")
                .append(receiver.getFirstName())
                .append("\n").append(" • 👤 Прізвище: ").append(receiver.getLastName()).append("\n");

        builder.append("\n");
        builder.append("Адреса отримувача 🏠\n").append(" • 🌍 Країна: ").append(telegramParcelDto.getDestinationCountry()).append("\n").append(" • 🏙️ Місто: ")
                .append(telegramParcelDto.getDestinationCity()).append("\n");

        if (telegramParcelDto.getDestinationStreet() != null) {
            builder.append(" • 🏢 Вулиця: ").append(telegramParcelDto.getDestinationStreet()).append("\n");
        }
        if (telegramParcelDto.getDestinationHouseNumber() != null) {
            builder.append(" • 🏡 Номер будинку: ").append(telegramParcelDto.getDestinationHouseNumber()).append("\n");
        }
        if (telegramParcelDto.getDestinationFlatNumber() != null) {
            builder.append(" • 🏠 Номер квартири: ").append(telegramParcelDto.getDestinationFlatNumber()).append("\n");
        }
        if (telegramParcelDto.getDeliveryService() != null) {
            builder.append(" • 📧 Пошта: ").append(telegramParcelDto.getDeliveryService()).append("\n");
        }
        if (telegramParcelDto.getDestinationPostNumber() != null) {
            builder.append(" • 🏤 Номер поштового відділення: ").append(telegramParcelDto.getDestinationPostNumber()).append("\n");
        }

        builder.append("\n");
        builder.append("Інформація про посилку 📦\n").append(" • 📦 Кількість місць: ").append(telegramParcelDto.getAmount()).append("\n");

        if (telegramParcelDto.getWeight() != null) {
            builder.append(" • ⚖️ Вага: ").append(telegramParcelDto.getWeight()).append("\n");
        }
        if (telegramParcelDto.getPrice() != null) {
            builder.append(" • 💲 Ціна: ").append(telegramParcelDto.getPrice()).append("\n");
        }
        return builder;
    }
}
