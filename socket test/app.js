let client = null;

let deviceId = 1;

// Guarda os chunks PCM recebidos
let audioChunks = [];

let receivingAudio = false;


// =====================================================
// LOG
// =====================================================

function log(message) {

    const logs = document.getElementById("logs");

    const time = new Date().toLocaleTimeString();

    logs.textContent += `[${time}] ${message}\n`;

    logs.scrollTop = logs.scrollHeight;
}


// =====================================================
// STATUS
// =====================================================

function setStatus(message) {

    document.getElementById("status").textContent = message;
}


// =====================================================
// CONECTAR AO MQTT
// =====================================================

function connectMQTT() {

    const broker =
        document.getElementById("broker").value;

    deviceId =
        document.getElementById("deviceId").value;


    if (!broker) {

        alert("Informe o endereço do broker.");

        return;
    }


    log(`Conectando ao broker: ${broker}`);


    // Se já existir uma conexão, encerra
    if (client) {

        client.end(true);

        client = null;
    }


    client = mqtt.connect(broker, {

        clientId:
            "neva-web-tester-" +
            Math.random()
                .toString(16)
                .substring(2),

        clean: true,

        // Não utiliza usuário ou senha
        username: undefined,

        password: undefined,

        // Não ficar tentando reconectar infinitamente
        reconnectPeriod: 0
    });


    // =================================================
    // CONECTADO
    // =================================================

    client.on("connect", () => {

        log("✓ Conectado ao MQTT");

        setStatus("🟢 Conectado");


        const topic =
            `neva/${deviceId}/audio/tts/#`;


        client.subscribe(
            topic,
            {
                qos: 1
            },
            (error) => {

                if (error) {

                    log(
                        "❌ Erro ao assinar tópico: " +
                        error.message
                    );

                    return;
                }


                log(
                    `✓ Inscrito em: ${topic}`
                );
            }
        );
    });


    // =================================================
    // MENSAGEM RECEBIDA
    // =================================================

    client.on(
        "message",
        handleMessage
    );


    // =================================================
    // ERRO
    // =================================================

    client.on(
        "error",
        (error) => {

            log(
                "❌ MQTT Error: " +
                error.message
            );

            setStatus("🔴 Erro MQTT");
        }
    );


    // =================================================
    // CONEXÃO ENCERRADA
    // =================================================

    client.on(
        "close",
        () => {

            log(
                "🔴 Conexão MQTT encerrada"
            );

            setStatus(
                "🔴 Desconectado"
            );
        }
    );
}


// =====================================================
// ENVIAR MENSAGEM PARA A NEVA
// =====================================================

function sendMessage() {

    if (!client || !client.connected) {

        alert(
            "Conecte ao MQTT primeiro."
        );

        return;
    }


    const message =
        document.getElementById("message").value;


    if (!message.trim()) {

        alert(
            "Digite uma mensagem."
        );

        return;
    }


    const topic =
        `neva/${deviceId}/chat/message`;


    const payload =
        JSON.stringify({

            JOIId: Number(deviceId),

            message: message
        });


    log(
        `→ Publicando em: ${topic}`
    );

    log(
        `→ Payload: ${payload}`
    );


    client.publish(
        topic,
        payload,
        {
            qos: 1
        },
        (error) => {

            if (error) {

                log(
                    "❌ Erro ao publicar: " +
                    error.message
                );

                return;
            }


            log(
                "✓ Mensagem enviada para a NEVA"
            );
        }
    );


    document.getElementById(
        "message"
    ).value = "";
}


// =====================================================
// PROCESSAR MENSAGEM MQTT
// =====================================================

function handleMessage(
    topic,
    payload
) {

    log(
        `← MQTT: ${topic}`
    );


    const startTopic =
        `neva/${deviceId}/audio/tts/start`;

    const dataTopic =
        `neva/${deviceId}/audio/tts/data`;

    const endTopic =
        `neva/${deviceId}/audio/tts/end`;


    // =================================================
    // START
    // =================================================

    if (topic === startTopic) {

        startAudio();

        return;
    }


    // =================================================
    // DATA
    // =================================================

    if (topic === dataTopic) {

        receiveAudioChunk(
            payload
        );

        return;
    }


    // =================================================
    // END
    // =================================================

    if (topic === endTopic) {

        finishAudio();

        return;
    }
}


// =====================================================
// INICIAR RECEBIMENTO DO ÁUDIO
// =====================================================

function startAudio() {

    log(
        "🔊 Início da transmissão de áudio"
    );


    audioChunks = [];

    receivingAudio = true;
}


// =====================================================
// RECEBER CHUNK
// =====================================================

function receiveAudioChunk(
    payload
) {

    if (!receivingAudio) {

        log(
            "⚠ Chunk recebido fora de uma transmissão"
        );

        return;
    }


    /*
     * MQTT.js entrega o payload como Uint8Array.
     *
     * Copiamos o conteúdo para evitar problemas
     * caso o buffer original seja reutilizado.
     */

    const chunk =
        new Uint8Array(
            payload
        );


    const copy =
        new Uint8Array(
            chunk.length
        );


    copy.set(chunk);


    audioChunks.push(copy);


    log(
        `   📦 Chunk recebido: ${copy.length} bytes`
    );
}


// =====================================================
// FINALIZAR ÁUDIO
// =====================================================

function finishAudio() {

    if (!receivingAudio) {

        return;
    }


    receivingAudio = false;


    log(
        "🔊 Fim da transmissão de áudio"
    );


    log(
        `📦 Total de chunks: ${audioChunks.length}`
    );


    const totalBytes =
        audioChunks.reduce(
            (total, chunk) =>
                total + chunk.length,
            0
        );


    log(
        `📦 Total de bytes: ${totalBytes}`
    );


    const audioBlob =
        createAudioBlob();


    if (!audioBlob) {

        log(
            "❌ Não foi possível criar o áudio."
        );

        return;
    }


    log(
        "✓ WAV criado com sucesso"
    );


    const audioUrl =
        URL.createObjectURL(
            audioBlob
        );


    const audio =
        new Audio(audioUrl);


    audio.volume = 1.0;


    audio.onplay = () => {

        log(
            "🔊 Reproduzindo resposta da NEVA..."
        );
    };


    audio.onended = () => {

        log(
            "✓ Reprodução finalizada"
        );


        URL.revokeObjectURL(
            audioUrl
        );
    };


    audio.onerror = (error) => {

        log(
            "❌ Erro ao reproduzir áudio"
        );

        console.error(
            error
        );


        URL.revokeObjectURL(
            audioUrl
        );
    };


    audio.play()
        .then(() => {

            log(
                "▶ Áudio reproduzindo"
            );

        })
        .catch(
            (error) => {

                log(
                    "❌ Navegador bloqueou a reprodução: " +
                    error.message
                );

                /*
                 * Em alguns navegadores, a reprodução
                 * automática pode ser bloqueada.
                 *
                 * Nesse caso o usuário pode precisar
                 * interagir com a página antes.
                 */
            }
        );
}


// =====================================================
// CRIAR WAV
// =====================================================

function createAudioBlob() {

    if (
        !audioChunks ||
        audioChunks.length === 0
    ) {

        return null;
    }


    // -----------------------------------------------
    // Juntar os chunks PCM
    // -----------------------------------------------

    const totalLength =
        audioChunks.reduce(
            (total, chunk) =>
                total + chunk.length,
            0
        );


    const pcmData =
        new Uint8Array(
            totalLength
        );


    let offset = 0;


    for (
        const chunk of audioChunks
    ) {

        pcmData.set(
            chunk,
            offset
        );


        offset += chunk.length;
    }


    // -----------------------------------------------
    // Configuração do Azure TTS
    //
    // Raw24Khz16BitMonoPcm
    // -----------------------------------------------

    const sampleRate = 24000;

    const numChannels = 1;

    const bitsPerSample = 16;


    const wavBuffer =
        createWav(
            pcmData,
            sampleRate,
            numChannels,
            bitsPerSample
        );


    return new Blob(
        [wavBuffer],
        {
            type: "audio/wav"
        }
    );
}


// =====================================================
// CONSTRUIR CABEÇALHO WAV
// =====================================================

function createWav(
    pcmData,
    sampleRate,
    numChannels,
    bitsPerSample
) {

    const bytesPerSample =
        bitsPerSample / 8;


    const byteRate =
        sampleRate *
        numChannels *
        bytesPerSample;


    const blockAlign =
        numChannels *
        bytesPerSample;


    /*
     * WAV PCM básico possui 44 bytes
     * de cabeçalho.
     */

    const buffer =
        new ArrayBuffer(
            44 + pcmData.length
        );


    const view =
        new DataView(
            buffer
        );


    // -----------------------------------------------
    // RIFF
    // -----------------------------------------------

    writeString(
        view,
        0,
        "RIFF"
    );


    view.setUint32(
        4,
        36 + pcmData.length,
        true
    );


    // -----------------------------------------------
    // WAVE
    // -----------------------------------------------

    writeString(
        view,
        8,
        "WAVE"
    );


    // -----------------------------------------------
    // fmt
    // -----------------------------------------------

    writeString(
        view,
        12,
        "fmt "
    );


    // Tamanho do bloco fmt
    view.setUint32(
        16,
        16,
        true
    );


    // Audio format
    // 1 = PCM
    view.setUint16(
        20,
        1,
        true
    );


    // Número de canais
    view.setUint16(
        22,
        numChannels,
        true
    );


    // Sample rate
    view.setUint32(
        24,
        sampleRate,
        true
    );


    // Byte rate
    view.setUint32(
        28,
        byteRate,
        true
    );


    // Block align
    view.setUint16(
        32,
        blockAlign,
        true
    );


    // Bits per sample
    view.setUint16(
        34,
        bitsPerSample,
        true
    );


    // -----------------------------------------------
    // data
    // -----------------------------------------------

    writeString(
        view,
        36,
        "data"
    );


    view.setUint32(
        40,
        pcmData.length,
        true
    );


    // -----------------------------------------------
    // PCM DATA
    // -----------------------------------------------

    const pcmBytes =
        new Uint8Array(
            buffer,
            44
        );


    pcmBytes.set(
        pcmData
    );


    return buffer;
}


// =====================================================
// ESCREVER STRING NO WAV
// =====================================================

function writeString(
    view,
    offset,
    string
) {

    for (
        let i = 0;
        i < string.length;
        i++
    ) {

        view.setUint8(
            offset + i,
            string.charCodeAt(i)
        );
    }
}

