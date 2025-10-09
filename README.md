# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4wAET9lGjAALYo47Xj4wA0E7jqAO7QHIvLa+Mos8BICHvjMAC+mMI1MBWs7FyU9SOTAzPz5wcbqttQuyWqwmRxOZyB1zYnG4sAet1E9SgmWyYEoAAoMlkcpQMgBHVI5ACUN2qonulVk8iUKnU9XsKDAAFUBmiplBPihiZTFMo1KpyUYdPUAGJITgwZmUbkwHSWGBsjmYYAIZhsmDKpHADhylAADxRGhgMm01L59zhpJU9UlUG5mCN8JU5sqtxeMECTmCwzG43m6mA9MWEwAolBvHV1QhNdqYPIANboC7XB3VSjO+DIcz1D1e0YTP2qAMLerjUPh+oalBauXxxNXMwIAwwNAQZgAM18nHtxqpvPU6cdKHqaB8CAQ3cHAu5ptpMBAmtRNrRNu5xKN077-IexnqCg4HAlAztKZEToeG5pqnq86rqIUPjA8TR98fq96PRg9LQlDkhkZjIASWkWMIATQp1xNTcBR3GA9wPF94mPKpT0MUoHihZ4I0xFEcTUUcsCNDCYQHVMIx9BU5mLfYJmAB94naUDEyBesTzuNDKmKLMYHCJwnG9CYKK+IEDlox8GLA8563QDgzC7TxvD8fxoHYekYmFOBg2kOAFBgAAZCAskKTiyhdUj6maNoul6Ax1HyNB+PeaZKO+dZ9D+HZJOuV1YUqIi3TeQSqOBcZfn+QFlkhJ5iItFD6gQAyxTRfTDLxAkwGJSdz0gy86QZJdAq5bKzW3IUYFFcUV20GU5UCpUVWbCB5QGGAOx8LsIN7S8SNi5tR3HViyXY5CamzJwAEYHILIsg1LMNoHqNrRPiaAkAALxQXYWOGtMhuMsBRomvNfT5aaSzLeb5RmOiVvWzbrhQRtDDVVr2p7HkuqGwdrSPbRu2VZhQp2GBZmyY4wBAeJuwGs8YpG91xsmk7AzOuaI0W66oDWjakyh7bYQePaDsR-1kZDVGFqux8buxraHqbKhlSQA8bw4cwkGVQ0jWGwaKSK2cb1-BDnzot8eg-b9NhAsDcYvYrKhguDYJF36ue5mHfKit1krFDJVHw3G-Px0zqH8gSPmc4SaLo8SmOWLbWJ2gnMwweoeL4o7Apc8YlpttBPO7aTZJkwP5N8AIvBQdAYjiRII6j5LfCwPaBW88zpGDXTg3aYNuh6GzVDs4YWCrVmoCvdUMbWvICnqAAeH3GLQcoSRNnzHmhN0lpu6u0DrhuwObw2U8tIcYHi+xE6SgzE9StR0pbqgebemdy-pMAhf79BCs6uXBRFMV4OV+Rqql9A6uYSAwJgMUGeQGSOve4rYYRXqxwnEfurhj1Dp9KbSdm8sl0u6Y1ujjB2RsMwlH2vDH++YkbFjJoA9GVMQE03uo9GAt8man3AsvKCn0R67iPsAd+KEpx83Lqzbgd46Jok3mgNceCPry1KhkWYEAaBK1fCrNWqF0KawjAnB8ut9aEQEZ-fyyY8bpkJtxXi3p7pdhDl4MO-gkQHn8NgMUcY9IohgAAcUohoZOsM3QNH0VnXO9hKJFxLpQcuwCq4EBrjAeu1tG7N28gKQ2FZK6rR7n3dxA9MBD2flaMeKJDF+iSpEoxs8iQLyXrLWca8N5BK3pgZJW4WH73FAhaUsocHnxwdfNAWD75MKfsbRe4SRxv2hnw6pbpv7E0LP-c6aNKbLVQXdUhbEnZQKJkdP+CCAEXWQd0rGvS6aGHKUUh+K9P4v3yTwzKvMd6zmQDkKJag0SMKydBVhDIfDskjAgAxlEkIHIIT1XSKJa47O5M3Bp6YfG6O2XEvCKpuyhNMWRCY1i-RBgaDAH0gKUBAUWGNcIwRAgHE2PEJAi5zZCWouMZIoA4w2g5F7cFAA5C2dtLhrBcKSmA3QpFeN2s7aBbsHLgtUMCmApKXDkqkkouSKjFIcAAOxuCcCgJwMRgzBDgBpAAbPABchgdkwCKDS4erdzKtA6FYmxaBi5ansb4lBTi7KBLEh4+llECWopxlSjWHcIyOP8c43urj6HlGNfMU1QV6yhOqS-AWqIdlojgNKnZ8T55rMqSkhkaTDVgW3o-fsJVcmH24cfQpl8z7-RKTfRmFTrlhNHnU-qvCJERhacM+BM0OkUxtWgvpjsOI0qGb-UtKMkFdOptMjBcyU24OzZ68JKz5B-XqiDWyDgegZrvjKCA2pcaZT+aNd2DaSajPLTKLUMAkT4jnmArmVKBlcQ9POuBi6y3kxXQeddaUt0zJgKBadH8sobPLt6lAvrHnaEYQs-BOTYL7gufMaUdDbL2DQJ+SiQF32hq3DmukoHpC3rIUNN5-rbzPsoiI75qtflNP+eMcFkL6jQthfbaR1LBlyIPThmDQYCOBFphy4OnKFIBEsA9eKktYhIASGAZjY4ICSwAFIQDFL+ww-gMUgDjPKqBiq4bNEZFZHo4LbFarLjqyZtr9UOvSU3By2AEDAGY1AOAEB4pQD2AAdRYABbO4wejjAAEK6QUHAAA0sFXD0goUwpo15Ui3iBGqe7nag19EjVHV0-pygRmTO4so7Uaj7rxFQZgAAK0E2gX1AmxSBpQBuhJIasm5XXrQ+h0bFlxrKgfLhiEqrJsbsUztpTyndm7bw4cfU+mDVnTA1pp1EHjJbT0rdO7a2keLQutpS6T0TNbTjK9Ha6sfo+klvtJCXn3pjavBkvr3P7IoYc+NwmClyncwO5g4KGrtk7BUxbVTWuv3zWtrr+6evtJPXoM9OWL1EeG5Avd8iS1HqbRdd7a7Pubtphg87L1rsQaWeE19-bVZpvPXPDac5jPQEjNGGswBpaq0e1h0auZxu9bGdaqMJcccSSI+AmRdb4bE8PRN49gDKzVljLjusUijRXpsK2FqSIxD44LetleC1sBaB9ZRADBcgMwHCwZkC6OTPgZu7Gr9jIJe-kOzw1WM7LWYXqJltAaGCL48w3jV43Pt2+ZI1xOloxreBx53RoOpguXhygPp6OHHY5e7lIgKssBgDYF04QHucqTGE8aOnTO2dc7GAXjtA3MJ6hohbBO7UxIPV3ZANwPAezElnnWRt68+eoCF+uV+thHDDDKnOZVeQawEfADWCt4petNzNb2zcmpo8WjOY6+rduhuMxB9Nz8xLXWRiUtt7ul2ZGFENibG2MEJyhci66wAVn+yT17gC0Ss1UPOYgdrr1thgJQcMhIhtz5G1xHf5GRks4uoftQJ-sBn4gBfq-0Ab-svoxkiAA)

[Link to Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4wAET9lGjAALYo47Xj4wA0E7jqAO7QHIvLa+Mos8BICHvjMAC+mMI1MBWs7FyU9SOTAzPz5wcbqttQuyWqwmRxOZyB1zYnG4sAet1E9SgmWyYEoAAoMlkcpQMgBHVI5ACUN2qonulVk8iUKnU9XsKDAAFUBmiplBPihiZTFMo1KpyUYdPUAGJITgwZmUbkwHSWGBsjmYYAIZhsmDKpHADhylAADxRGhgMm01L59zhpJU9UlUG5mCN8JU5sqtxeMECTmCwzG43m6mA9MWEwAolBvHV1QhNdqYPIANboC7XB3VSjO+DIcz1D1e0YTP2qAMLerjUPh+oalBauXxxNXMwIAwwNAQZgAM18nHtxqpvPU6cdKHqaB8CAQ3cHAu5ptpMBAmtRNrRNu5xKN077-IexnqCg4HAlAztKZEToeG5pqnq86rqIUPjA8TR98fq96PRg9LQlDkhkZjIASWkWMIATQp1xNTcBR3GA9wPF94mPKpT0MUoHihZ4I0xFEcTUUcsCNDCYQHVMIx9BU5mLfYJmAB94naUDEyBesTzuNDKmKLMYHCJwnG9CYKK+IEDlox8GLA8563QDgzC7TxvD8fxoHYekYmFOBg2kOAFBgAAZCAskKTiyhdUj6maNoul6Ax1HyNB+PeaZKO+dZ9D+HZJOuV1YUqIi3TeQSqOBcZfn+QFlkhJ5iItFD6gQAyxTRfTDLxAkwGJSdz0gy86QZJdAq5bKzW3IUYFFcUV20GU5UCpUVWbCB5QGGAOx8LsIN7S8SNi5tR3HViyXY5CamzJwAEYHILIsg1LMNoHqNrRPiaAkAALxQXYWOGtMhuMsBRomvNfT5aaSzLeb5RmOiVvWzbrhQRtDDVVr2p7HkuqGwdrSPbRu2VZhQp2GBZmyY4wBAeJuwGs8YpG91xsmk7AzOuaI0W66oDWjakyh7bYQePaDsR-1kZDVGFqux8buxraHqbKhlSQA8bw4cwkGVQ0jWGwaKSK2cb1-BDnzot8eg-b9NhAsDcYvYrKhguDYJF36ue5mHfKit1krFDJVHw3G-Px0zqH8gSPmc4SaLo8SmOWLbWJ2gnMwweoeL4o7Apc8YlpttBPO7aTZJkwP5N8AIvBQdAYjiRII6j5LfCwPaBW88zpGDXTg3aYNuh6GzVDs4YWCrVmoCvdUMbWvICnqAAeH3GLQcoSRNnzHmhN0lpu6u0DrhuwObw2U8tIcYHi+xE6SgzE9StR0pbqgebemdy-pMAhf79BCs6uXBRFMV4OV+Rqql9A6uYSAwJgMUGeQGSOve4rYYRXqxwnEfurhj1Dp9KbSdm8sl0u6Y1ujjB2RsMwlH2vDH++YkbFjJoA9GVMQE03uo9GAt8man3AsvKCn0R67iPsAd+KEpx83Lqzbgd46Jok3mgNceCPry1KhkWYEAaBK1fCrNWqF0KawjAnB8ut9aEQEZ-fyyY8bpkJtxXi3p7pdhDl4MO-gkQHn8NgMUcY9IohgAAcUohoZOsM3QNH0VnXO9hKJFxLpQcuwCq4EBrjAeu1tG7N28gKQ2FZK6rR7n3dxA9MBD2flaMeKJDF+iSpEoxs8iQLyXrLWca8N5BK3pgZJW4WH73FAhaUsocHnxwdfNAWD75MKfsbRe4SRxv2hnw6pbpv7E0LP-c6aNKbLVQXdUhbEnZQKJkdP+CCAEXWQd0rGvS6aGHKUUh+K9P4v3yTwzKvMd6zmQDkKJag0SMKydBVhDIfDskjAgAxlEkIHIIT1XSKJa47O5M3Bp6YfG6O2XEvCKpuyhNMWRCY1i-RBgaDAH0gKUBAUWGNcIwRAgHE2PEJAi5zZCWouMZIoA4w2g5F7cFAA5C2dtLhrBcKSmA3QpFeN2s7aBbsHLgtUMCmApKXDkqkkouSKjFIcAAOxuCcCgJwMRgzBDgBpAAbPABchgdkwCKDS4erdzKtA6FYmxaBi5ansb4lBTi7KBLEh4+llECWopxlSjWHcIyOP8c43urj6HlGNfMU1QV6yhOqS-AWqIdlojgNKnZ8T55rMqSkhkaTDVgW3o-fsJVcmH24cfQpl8z7-RKTfRmFTrlhNHnU-qvCJERhacM+BM0OkUxtWgvpjsOI0qGb-UtKMkFdOptMjBcyU24OzZ68JKz5B-XqiDWyDgegZrvjKCA2pcaZT+aNd2DaSajPLTKLUMAkT4jnmArmVKBlcQ9POuBi6y3kxXQeddaUt0zJgKBadH8sobPLt6lAvrHnaEYQs-BOTYL7gufMaUdDbL2DQJ+SiQF32hq3DmukoHpC3rIUNN5-rbzPsoiI75qtflNP+eMcFkL6jQthfbaR1LBlyIPThmDQYCOBFphy4OnKFIBEsA9eKktYhIASGAZjY4ICSwAFIQDFL+ww-gMUgDjPKqBiq4bNEZFZHo4LbFarLjqyZtr9UOvSU3By2AEDAGY1AOAEB4pQD2AAdRYABbO4wejjAAEK6QUHAAA0sFXD0goUwpo15Ui3iBGqe7nag19EjVHV0-pygRmTO4so7Uaj7rxFQZgAAK0E2gX1AmxSBpQBuhJIasm5XXrQ+h0bFlxrKgfLhiEqrJsbsUztpTyndm7bw4cfU+mDVnTA1pp1EHjJbT0rdO7a2keLQutpS6T0TNbTjK9Ha6sfo+klvtJCXn3pjavBkvr3P7IoYc+NwmClyncwO5g4KGrtk7BUxbVTWuv3zWtrr+6evtJPXoM9OWL1EeG5Avd8iS1HqbRdd7a7Pubtphg87L1rsQaWeE19-bVZpvPXPDac5jPQEjNGGswBpaq0e1h0auZxu9bGdaqMJcccSSI+AmRdb4bE8PRN49gDKzVljLjusUijRXpsK2FqSIxD44LetleC1sBaB9ZRADBcgMwHCwZkC6OTPgZu7Gr9jIJe-kOzw1WM7LWYXqJltAaGCL48w3jV43Pt2+ZI1xOloxreBx53RoOpguXhygPp6OHHY5e7lIgKssBgDYF04QHucqTGE8aOnTO2dc7GAXjtA3MJ6hohbBO7UxIPV3ZANwPAezElnnWRt68+eoCF+uV+thHDDDKnOZVeQawEfADWCt4petNzNb2zcmpo8WjOY6+rduhuMxB9Nz8xLXWRiUtt7ul2ZGFENibG2MEJyhci66wAVn+yT17gC0Ss1UPOYgdrr1thgJQcMhIhtz5G1xHf5GRks4uoftQJ-sBn4gBfq-0Ab-svoxkiAA)


## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
